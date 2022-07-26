package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.SoccerLeague

data class SoccerLeagueDto(
    val league_id: Int,
    val name: String,
    val country: String
) {
    fun toSoccerLeague(): SoccerLeague {
        return SoccerLeague(
            leagueId = league_id,
            name = name,
            country = country
        )
    }
}
