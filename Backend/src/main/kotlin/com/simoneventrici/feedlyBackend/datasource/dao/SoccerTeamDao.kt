package com.simoneventrici.feedlyBackend.datasource.dao

import com.simoneventrici.feedlyBackend.model.SoccerTeam
import com.simoneventrici.feedlyBackend.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.PreparedStatementSetter
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
class SoccerTeamDao(
    @Autowired val jdbcTemplate: JdbcTemplate
): Dao<SoccerTeam> {

    private val getAllQuery = "select * from soccer_team"
    private val saveQuery = "insert into soccer_team values(?,?)"
    private val saveUserFavouriteTeamQuery = "insert  into user_like_soccer_team values(?,?) on conflict do nothing"
    private val removeAllUserFavouriteTeamQuery = "delete from user_like_soccer_team where \"user\"=?"
    private val userFavouriteTeamsQuery = "select * from user_like_soccer_team where \"user\"=?"

    override fun getAll(): List<SoccerTeam> {
        val list = mutableListOf<SoccerTeam>()
        jdbcTemplate.query(getAllQuery) {
            list.add(SoccerTeam.fromResultSet(it))
        }
        return list
    }

    override fun save(elem: SoccerTeam) {
       jdbcTemplate.execute(saveQuery) {
           it.setInt(1, elem.teamId)
           it.setString(2, elem.name)
           it.execute()
       }
    }

    fun getUserFavouriteTeams(user: User): List<Int> {
        val list = mutableListOf<Int>()
        val creator = PreparedStatementCreator { it.prepareStatement(userFavouriteTeamsQuery) }
        val setter = PreparedStatementSetter { it.setString(1, user.getUsername()) }
        jdbcTemplate.query(creator, setter) { rs ->
            while(rs.next()) { list.add(rs.getInt(2)) }
            rs.close()
        }
        return list
    }

    fun setFavouriteTeams(user: User, teamIds: List<Int>) {
        // prima rimuovo tutti i team preferiti dall'utente
        jdbcTemplate.execute(removeAllUserFavouriteTeamQuery) {
            it.setString(1, user.getUsername())
            it.execute()
        }

        // dopo inserisco i nuovi
        teamIds.forEach { id ->
            jdbcTemplate.execute(saveUserFavouriteTeamQuery) {
                it.setString(1, user.getUsername())
                it.setInt(2, id)
                it.execute()
            }
        }
    }
}