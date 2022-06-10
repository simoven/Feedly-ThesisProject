package com.simoneventrici.feedly.remote.dto.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)