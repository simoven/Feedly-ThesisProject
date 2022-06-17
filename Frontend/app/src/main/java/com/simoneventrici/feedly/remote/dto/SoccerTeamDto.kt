package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.SoccerTeam

data class SoccerTeamDto(
    val team_id: Int,
    val name: String
) {
    fun toSoccerTeam(): SoccerTeam {
        return SoccerTeam(
            teamId = team_id,
            name = name
        )
    }
}