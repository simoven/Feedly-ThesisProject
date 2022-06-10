package com.simoneventrici.feedly.remote.dto.weather

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)