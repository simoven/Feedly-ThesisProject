package com.simoneventrici.feedly.remote.dto

import com.google.gson.annotations.SerializedName
import com.simoneventrici.feedly.model.CryptoMarketStats

data class MarketStatsDto(
    @SerializedName("data") val marketData: Data
) {
    fun toMarketStats(): CryptoMarketStats {
        return CryptoMarketStats(
            btcDominance = marketData.btcDominance,
            total24hVolume = marketData.total24hVolume.toDouble(),
            totalCoins = marketData.totalCoins,
            totalMarketCap = marketData.totalMarketCap.toDouble()
        )
    }
}