package com.simoneventrici.feedlyBackend.datasource.dto.soccer

import com.simoneventrici.feedlyBackend.model.TeamMatch

data class Response(
    val fixture: Fixture,
    val goals: Goals,
    val league: League,
    val score: Score,
    val teams: Teams
) {
    fun toTeamMatch(): TeamMatch {
        return TeamMatch(
            homeTeamId = teams.home.id,
            awayTeamId = teams.away.id,
            homeTeamName = teams.home.name,
            awayTeamName = teams.away.name,
            leagueId = league.id,
            leagueName = league.name,
            homeScore = score.fulltime.home,
            awayScore = score.fulltime.away,
            timestamp = fixture.timestamp.toLong()
        )
    }
}