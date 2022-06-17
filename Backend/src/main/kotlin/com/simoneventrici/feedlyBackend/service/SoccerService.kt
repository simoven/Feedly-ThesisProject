package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.datasource.api.FootballAPI
import com.simoneventrici.feedlyBackend.datasource.dao.SoccerLeagueDao
import com.simoneventrici.feedlyBackend.datasource.dao.SoccerTeamDao
import com.simoneventrici.feedlyBackend.model.*
import org.springframework.stereotype.Service

@Service
class SoccerService(
    private val soccerTeamDao: SoccerTeamDao,
    private val soccerLeagueDao: SoccerLeagueDao,
    private val footballAPI: FootballAPI
) {
    val allTeams: List<SoccerTeam>
    val allLeagues: List<SoccerLeague>

    val allMatchesByTeamId: MutableMap<Int, List<TeamMatch>> = mutableMapOf()
    val allStandingsByLeagueId: MutableMap<Int, List<LeagueStandings>> = mutableMapOf()
    val currentYear: Int = 2021

    // 20 API Calls per le squadre
    // 5 API Calls per le classifiche
    // Limite 100 Calls/Day

    init {
        allTeams = soccerTeamDao.getAll()
        allLeagues = soccerLeagueDao.getAll()
        allTeams.forEach { it.playsInLeagueInYear = soccerLeagueDao.getLeaguePlayedByTeamInYear(it.teamId, allLeagues) }
    }

    fun getMatchesByTeamId(teamId: Int): List<TeamMatch> {
        return allMatchesByTeamId[teamId] ?: emptyList()
    }

    fun getStandingsByLeagueId(leagueId: Int): List<LeagueStandings> {
        return allStandingsByLeagueId[leagueId] ?: emptyList()
    }

    fun setUserFavouriteTeam(user: User, teamIds: List<Int>) {
        soccerTeamDao.setFavouriteTeams(user, teamIds)
    }

    fun fetchAllTeamMatches() {
        // per ogni team che gioca in serie A, fetcho la lista degli ultimi incontri e rimane salvata
        val tempTeams = listOf(489, 505, 499)
        /*allTeams.filter { it.playsInLeagueInYear[currentYear]?.leagueId == 135 }*/tempTeams.forEach { team ->
            val result = footballAPI.getMatchesByTeamId(team, currentYear)
            result.body?.let {
                allMatchesByTeamId[team] = it.response.map { resp -> resp.toTeamMatch() }
            }
        }
    }

    fun getUserFavouriteTeamsMatches(user: User): List<TeamMatch> {
        val favTeams = soccerTeamDao.getUserFavouriteTeams(user)
        val matchList = mutableListOf<TeamMatch>()

        favTeams.forEach { teamId -> matchList.addAll(allMatchesByTeamId[teamId] ?: emptyList()) }
        return matchList.sortedByDescending { it.timestamp }
    }

    fun fetchAllLeaguesStandings() {
        // per ogni lega, fetcho la classifica attuale
        //allLeagues.forEach { league ->
        listOf(SoccerLeague(135, "", "")).forEach { league ->
            val result = footballAPI.getStandingsByLeagueId(league.leagueId, currentYear)
            result.body?.let {
                allStandingsByLeagueId[league.leagueId] = it.response[0].league.standings[0].map { st -> st.toLeagueStanding() }
            }
        }
    }

    fun getUserFavouriteTeam(user: User): List<SoccerTeam> {
        val result = soccerTeamDao.getUserFavouriteTeams(user)
        return result.mapNotNull {
            allTeams.find { team -> team.teamId == it }
        }
    }
}