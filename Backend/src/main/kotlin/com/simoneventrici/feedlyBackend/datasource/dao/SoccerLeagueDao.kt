package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.SoccerLeague
import com.simoneventrici.feedlyBackend.model.SoccerTeam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.*
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class SoccerLeagueDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<SoccerLeague> {

    private val getAllQuery = "select * from soccer_league"
    private val getLeaguePlayedByTeamQuery = "select * from team_plays_in_league where team_id=?"
    private val saveQuery = "insert into soccer_league values(?,?,?)"
    private val saveTeamPlaysInLeagueQuery = "insert into team_plays_in_league values(?,?,?) on conflict do nothing"

    override fun getAll(): List<SoccerLeague> {
        val list = mutableListOf<SoccerLeague>()
        jdbcTemplate.query(getAllQuery) {
            list.add(SoccerLeague.fromResultSet(it))
        }
        return list
    }

    // per una squadra, ottengo la mappa delle leghe in cui ha giocato in un determinato anno
    // questo perchè una squadra può retrocedere e quindi non giocare in quella lega in un determinato anno
    fun getLeaguePlayedByTeamInYear(teamId: Int, allLeagues: List<SoccerLeague>): Map<Int, SoccerLeague> {
        val getLeagueById: (id: Int) -> SoccerLeague? = { id -> allLeagues.firstOrNull { it.leagueId == id } }
        val map = mutableMapOf<Int, SoccerLeague>()
        val creator = PreparedStatementCreator { conn -> conn.prepareStatement(getLeaguePlayedByTeamQuery) }
        jdbcTemplate.query(creator, { it.setInt(1, teamId) }) { rs ->
            while(rs.next()) {
                val leagueId = rs.getInt("league_id")
                val year = rs.getInt("year")
                getLeagueById(leagueId)?.let {
                    map[year] = it
                }
            }
            rs.close()
        }
        return map
    }

    fun saveTeamPlaysInLeague(team: SoccerTeam, league: SoccerLeague, year: Int) {
        jdbcTemplate.execute(saveTeamPlaysInLeagueQuery) {
            it.setInt(1, team.teamId)
            it.setInt(2, league.leagueId)
            it.setInt(3, year)
            it.execute()
        }
    }

    override fun save(elem: SoccerLeague) {
       jdbcTemplate.execute(saveQuery) {
           it.setInt(1, elem.leagueId)
           it.setString(2, elem.name)
           it.setString(3, elem.country)
           it.execute()
       }
    }
}