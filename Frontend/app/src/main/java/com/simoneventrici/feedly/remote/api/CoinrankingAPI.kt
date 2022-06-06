package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.MarketStatsDto
import retrofit2.http.GET
import retrofit2.http.Header

interface CoinrankingAPI {

    @GET("stats")
    suspend fun getMarketGlobalData(
        @Header("x-access-token") authToken: String
    ): MarketStatsDto
}