package com.simoneventrici.feedly.model

data class GeoLocalizationInfo(
    val cityName: String,
    val region: String,
    val regionCode: String,
    val fullLabel: String,
    val latitude: Double,
    val longitude: Double
)