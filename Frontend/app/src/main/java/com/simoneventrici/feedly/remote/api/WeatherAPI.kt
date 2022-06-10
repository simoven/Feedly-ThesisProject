package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.weather.WeatherOverviewDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("onecall")
    suspend fun getWeatherInfoByCoords(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") authToken: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en"
    ): WeatherOverviewDto
}