package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.StockData

data class StockDataDto(
    val name: String,
    val ticker: String,
    val prices: List<Double>,
    val latest_close: Double,
    var change_24h: Double
) {
    fun toStockData(): StockData {
        return StockData(
            name = name,
            ticker = ticker,
            prices = prices,
            latestClose = latest_close,
            change24h = change_24h
        )
    }
}
