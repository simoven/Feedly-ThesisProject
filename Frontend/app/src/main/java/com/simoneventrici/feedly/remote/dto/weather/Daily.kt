package com.simoneventrici.feedly.remote.dto.weather

import com.simoneventrici.feedly.model.WeatherInfo

data class Daily(
    val clouds: Int,
    val dt: Long,
    val feels_like: FeelsLike,
    val humidity: Int,
    val moon_phase: Double,
    val moonrise: Int,
    val moonset: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val weather: List<Weather>,
    val wind_speed: Double
) {
    fun toWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            time = dt,
            temperature = temp.day,
            feelsLike = feels_like.day,
            windSpeed = wind_speed,
            weatherDescription = weather[0].description,
            weatherIconCode = weather[0].icon,
            minTemp = temp.min,
            maxTemp = temp.max
        )
    }
}