package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.CryptoMarketData

data class CryptoMarketDataDto(
    val ath: Double,
    val ath_change_percentage: Double,
    val ath_date: String,
    val atl: Double,
    val atl_change_percentage: Double,
    val atl_date: String,
    val circulating_supply: Double,
    val current_price: Double,
    val id: String,
    val image: String,
    val low_24h: Double,
    val market_cap: Double,
    val market_cap_change_percentage_24h: Double,
    val market_cap_rank: Int,
    val name: String,
    val price_change_24h: Double,
    val price_change_percentage_1h_in_currency: Double,
    val price_change_percentage_24h_in_currency: Double,
    val price_change_percentage_7d_in_currency: Double,
    val sparkline_in_7d: SparklineIn7d,
    val symbol: String,
    val total_volume: Double
) {
    fun toCryptoMarketData(): CryptoMarketData {
        return CryptoMarketData(
            rank = market_cap_rank,
            currentPrice = current_price,
            marketCap = market_cap,
            change1h = price_change_percentage_1h_in_currency,
            change24h = price_change_percentage_24h_in_currency,
            change7d = price_change_percentage_7d_in_currency,
            sparkline = sparkline_in_7d.price,
            ticker = symbol,
            imageUrl = image,
        )
    }
}