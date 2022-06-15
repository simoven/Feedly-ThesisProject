package com.simoneventrici.feedlyBackend.model

data class LeagueStandings(
    val rank: Int,
    val teamId: Int,
    val teamName: String,
    val points: Int,
    val played: Int,
    val matchesWon: Int,
    val matchesDraw: Int,
    val matchesLost: Int,
    val goalScored: Int,
    val goalAgainst: Int
) {
}