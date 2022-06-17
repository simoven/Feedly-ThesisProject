package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.LeagueStandings

data class LeagueStandingsDto(
    val rank: Int,
    val team_id: Int,
    val team_name: String,
    val points: Int,
    val played: Int,
    val matches_won: Int,
    val matches_draw: Int,
    val matches_lost: Int,
    val goal_scored: Int,
    val goal_against: Int
) {
    fun toLeagueStandings(): LeagueStandings {
        return LeagueStandings(
            rank = rank,
            teamId = team_id,
            teamName = team_name,
            points = points,
            played = played,
            matchesWon = matches_won,
            matchesDraw = matches_draw,
            matchesLost = matches_lost,
            goalScored = goal_scored,
            goalAgainst = goal_against
        )
    }
}