package com.simoneventrici.feedly.remote.dto.weather

import com.simoneventrici.feedly.model.WeatherInfo

data class Hourly(
    val clouds: Int,
    val dt: Long,
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Rain,
    val temp: Double,
    val weather: List<Weather>,
    val wind_gust: Double,
    val wind_speed: Double
) {
    fun toWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            time = dt,
            temperature = temp,
            feelsLike = feels_like,
            windSpeed = wind_speed,
            weatherDescription = weather[0].description,
            weatherIconCode = weather[0].icon,
            minTemp = null,
            maxTemp = null
        )
    }
}