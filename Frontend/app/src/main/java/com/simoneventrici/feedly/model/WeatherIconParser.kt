package com.simoneventrici.feedly.model

import com.simoneventrici.feedly.R

sealed class WeatherIconParser(val id: String) {
    class WeatherIcon(val res_id: Int, id: String): WeatherIconParser(id)

    companion object {
        fun parse(id: String): WeatherIcon {
            return when(id) {
                "01d" -> WeatherIcon(R.drawable.weather_01d, "01d")
                "02d" -> WeatherIcon(R.drawable.weather_02d, "02d")
                "03d" -> WeatherIcon(R.drawable.weather_03dn, "03d")
                "04d" -> WeatherIcon(R.drawable.weather_04dn, "04d")
                "09d" -> WeatherIcon(R.drawable.weather_09dn, "09d")
                "10d" -> WeatherIcon(R.drawable.weather_10d, "10d")
                "11d" -> WeatherIcon(R.drawable.weather_11dn, "11d")
                "13d" -> WeatherIcon(R.drawable.weather_13dn, "13d")
                "50d" -> WeatherIcon(R.drawable.weather_50dn, "50d")
                "01n" -> WeatherIcon(R.drawable.weather_01n, "01d")
                "02n" -> WeatherIcon(R.drawable.weather_02n, "03d")
                "03n" -> WeatherIcon(R.drawable.weather_03dn, "03d")
                "04n" -> WeatherIcon(R.drawable.weather_04dn, "04d")
                "09n" -> WeatherIcon(R.drawable.weather_09dn, "09d")
                "10n" -> WeatherIcon(R.drawable.weather_10n, "10d")
                "11n" -> WeatherIcon(R.drawable.weather_11dn, "11d")
                "13n" -> WeatherIcon(R.drawable.weather_13dn, "13d")
                "50n" -> WeatherIcon(R.drawable.weather_50dn, "50d")
                else -> WeatherIcon(R.drawable.no_weather, "00")
            }
        }
    }
}