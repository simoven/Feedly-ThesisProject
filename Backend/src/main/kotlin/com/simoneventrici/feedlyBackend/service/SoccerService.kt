package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.model.SoccerLeague
import com.simoneventrici.feedlyBackend.model.SoccerTeam
import com.simoneventrici.feedlyBackend.persistence.dao.SoccerLeagueDao
import com.simoneventrici.feedlyBackend.persistence.dao.SoccerTeamDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SoccerService(
    private val soccerTeamDao: SoccerTeamDao,
    private val soccerLeagueDao: SoccerLeagueDao
) {
    val allTeams: List<SoccerTeam>
    val allLeagues: List<SoccerLeague>

    init {
        allTeams = soccerTeamDao.getAll()
        allLeagues = soccerLeagueDao.getAll()
        allTeams.forEach { it.playsInLeagueInYear = soccerLeagueDao.getLeaguePlayedByTeamInYear(it.teamId, allLeagues) }
    }
}