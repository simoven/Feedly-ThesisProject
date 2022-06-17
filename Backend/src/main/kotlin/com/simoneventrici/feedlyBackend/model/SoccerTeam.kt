package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.ResultSet

data class SoccerTeam(
    @JsonProperty("team_id") val teamId: Int,
    val name: String,
    @JsonIgnore var playsInLeagueInYear: Map<Int, SoccerLeague> = emptyMap()
) {

    companion object {
        fun fromResultSet(rs: ResultSet): SoccerTeam {
            return SoccerTeam(
                teamId = rs.getInt(1),
                name = rs.getString(2),
            )
        }
    }
}