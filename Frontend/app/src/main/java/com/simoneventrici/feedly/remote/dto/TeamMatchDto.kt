package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.TeamMatch

data class TeamMatchDto(
    val home_team_id: Int,
    val away_team_id: Int,
    val home_team_name: String,
    val away_team_name: String,
    val league_name: String,
    val league_id: Int,
    val home_score: Int,
    val away_score: Int,
    val timestamp: Long,
) {
    fun toTeamMatch(): TeamMatch {
        return TeamMatch(
            homeTeamId = home_team_id,
            awayTeamId = away_team_id,
            homeTeamName = home_team_name,
            awayTeamName = away_team_name,
            leagueName = league_name,
            leagueId = league_id,
            homeScore = home_score,
            awayScore = away_score,
            timestamp = timestamp
        )
    }
}