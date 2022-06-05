package com.simoneventrici.feedly.model

data class CryptoMarketData(
    val ticker: String,
    val rank: Int,
    val currentPrice: Double,
    val marketCap: Double,
    val change1h: Double,
    val change24h: Double,
    val change7d: Double,
    val sparkline: List<Double>,
    val imageUrl: String
)
