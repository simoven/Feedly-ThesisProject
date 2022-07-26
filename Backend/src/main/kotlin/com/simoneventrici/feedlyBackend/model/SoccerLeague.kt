package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.ResultSet

data class SoccerLeague(
    @JsonProperty("league_id") val leagueId: Int,
    val name: String,
    val country: String
) {
    companion object {
        fun fromResultSet(rs: ResultSet): SoccerLeague {
            return SoccerLeague(
                leagueId = rs.getInt(1),
                name = rs.getString(2),
                country = rs.getString(3)
            )
        }
    }
}
