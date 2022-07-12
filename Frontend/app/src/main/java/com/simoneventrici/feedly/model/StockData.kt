package com.simoneventrici.feedly.model

data class StockData(
    val name: String,
    val ticker: String,
    val prices: List<Double>,
    val latestClose: Double,
    var change24h: Double
)
