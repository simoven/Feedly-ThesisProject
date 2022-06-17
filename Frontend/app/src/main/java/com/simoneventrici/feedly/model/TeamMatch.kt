package com.simoneventrici.feedly.model

data class TeamMatch(
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeTeamName: String,
    val awayTeamName: String,
    val leagueName: String,
    val leagueId: Int,
    val homeScore: Int,
    val awayScore: Int,
    val timestamp: Long,
) {
    fun getTeamLogoById(teamId: Int): String {
        return "https://media.api-sports.io/football/teams/$teamId.png"
    }
}