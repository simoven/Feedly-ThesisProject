package com.simoneventrici.feedly.model

data class SoccerTeam(
    val teamId: Int,
    val name: String
) {
    fun getTeamLogoById(): String {
        return "https://media.api-sports.io/football/teams/$teamId.png"
    }
}