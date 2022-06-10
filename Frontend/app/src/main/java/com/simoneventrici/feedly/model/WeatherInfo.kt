package com.simoneventrici.feedly.model

class WeatherInfo(
    val time: Long,
    val temperature: Double,
    val feelsLike: Double,
    val windSpeed: Double,
    val weatherIconCode: String,
    val weatherDescription: String,
    val minTemp: Double?,
    val maxTemp: Double?
)