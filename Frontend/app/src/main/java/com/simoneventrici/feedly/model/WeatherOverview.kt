package com.simoneventrici.feedly.model

data class WeatherOverview(
    val timezoneOffset: Int,
    val currentWeather: WeatherInfo,
    val hourlyWeather: List<WeatherInfo>,
    val dailyWeather: List<WeatherInfo>
)