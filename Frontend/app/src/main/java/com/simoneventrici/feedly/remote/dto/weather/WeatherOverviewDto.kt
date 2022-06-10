package com.simoneventrici.feedly.remote.dto.weather

import com.simoneventrici.feedly.model.WeatherOverview

data class WeatherOverviewDto(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
) {
    fun toWeatherOverview(): WeatherOverview {
        return WeatherOverview(
            timezoneOffset = timezone_offset,
            currentWeather = current.toWeatherInfo(),
            hourlyWeather = hourly.map { it.toWeatherInfo() },
            dailyWeather = daily.map { it.toWeatherInfo() }
        )
    }
}