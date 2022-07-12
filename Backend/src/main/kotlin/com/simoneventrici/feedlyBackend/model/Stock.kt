package com.simoneventrici.feedlyBackend.model

import java.sql.ResultSet

data class Stock(
    val ticker: String,
    val name: String
) {
    companion object {
        fun fromResultSet(rs: ResultSet): Stock {
            return Stock(
                ticker = rs.getString(1),
                name = rs.getString(2)
            )
        }
    }
}
