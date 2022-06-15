package com.simoneventrici.feedlyBackend.datasource.dto.soccer

import com.simoneventrici.feedlyBackend.model.LeagueStandings

data class Standing(
    val all: All,
    val form: String,
    val goalsDiff: Int,
    val group: String,
    val points: Int,
    val rank: Int,
    val status: String,
    val team: Team,
    val update: String
) {
    fun toLeagueStanding(): LeagueStandings {
        return LeagueStandings(
            rank = rank,
            teamId = team.id,
            teamName = team.name,
            points = points,
            played = all.played,
            matchesWon = all.win,
            matchesDraw = all.draw,
            matchesLost = all.lose,
            goalScored = all.goals.goalsFor,
            goalAgainst = all.goals.against
        )
    }
}