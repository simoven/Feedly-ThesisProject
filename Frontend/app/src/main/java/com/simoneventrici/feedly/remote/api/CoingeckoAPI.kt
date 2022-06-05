package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.CryptoMarketDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoingeckoAPI {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d")
    suspend fun getCoinsMarketData(
        @Query("ids") cryptoIds: String,
    ): List<CryptoMarketDataDto>
}