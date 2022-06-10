package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.weather.GeoLocalizationInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PositionStackAPI {

    @GET("forward")
    suspend fun getLocalizationInfoFromAddress(
        @Query("access_key") authToken: String,
        @Query("query") addressQuery: String
    ): GeoLocalizationInfoDto
}