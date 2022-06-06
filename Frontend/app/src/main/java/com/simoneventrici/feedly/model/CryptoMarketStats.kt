package com.simoneventrici.feedly.model

data class CryptoMarketStats(
    val btcDominance: Double,
    val total24hVolume: Double,
    val totalCoins: Int,
    val totalMarketCap: Double,
)