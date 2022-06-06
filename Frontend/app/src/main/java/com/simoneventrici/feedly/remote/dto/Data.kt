package com.simoneventrici.feedly.remote.dto

data class Data(
    val btcDominance: Double,
    val total24hVolume: String,
    val totalCoins: Int,
    val totalExchanges: Int,
    val totalMarketCap: String,
    val totalMarkets: Int
)