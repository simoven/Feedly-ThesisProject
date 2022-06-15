package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.sql.ResultSet

data class SoccerTeam(
    val teamId: Int,
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