package com.simoneventrici.feedlyBackend.datasource.dto.stocks

import com.simoneventrici.feedlyBackend.model.Stock
import com.simoneventrici.feedlyBackend.model.StocksData

data class StocksDataDto (
    val timestamp: Collection<Long>,
    val close: Collection<Double>,
    val symbol: String,
    val previousClose: Double,
    val end: Long,
    val start: Long
) {
    fun toStocksData(stock: Stock): StocksData {
        return StocksData(
            name = stock.name,
            ticker = stock.ticker,
            prices = close.toList(),
            latestClose = previousClose,
            change24h = 0.0
        )
    }
}