package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.Stock

data class StockDto(
    val ticker: String,
    val name: String
) {
    fun toStock(): Stock {
        return Stock(
            name = name,
            ticker = ticker
        )
    }
}
