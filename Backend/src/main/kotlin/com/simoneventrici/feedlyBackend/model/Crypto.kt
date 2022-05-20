package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.ResultSet

data class Crypto(
    val ticker: String,
    val name: String,
    @JsonProperty("stats_id") val statsId: String,
    @JsonProperty("graphic_id") val graphicId: Int
) {
    companion object {
        fun fromResultSet(rs: ResultSet): Crypto {
            return Crypto(
                ticker = rs.getString("ticker"),
                name = rs.getString("name"),
                statsId = rs.getString("stats_id"),
                graphicId = rs.getInt("graphic_id")
            )
        }
    }
}
