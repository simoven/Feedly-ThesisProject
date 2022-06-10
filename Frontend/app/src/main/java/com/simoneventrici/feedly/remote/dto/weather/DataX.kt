package com.simoneventrici.feedly.remote.dto.weather

import com.simoneventrici.feedly.model.GeoLocalizationInfo

data class DataX(
    val administrative_area: String,
    val continent: String,
    val country: String,
    val country_code: String,
    val label: String,
    val latitude: Double,
    val locality: String,
    val longitude: Double,
    val name: String,
    val region: String?,
    val region_code: String?,
    val type: String
) {
    fun toGeolocalizationInfo(): GeoLocalizationInfo {
        return GeoLocalizationInfo(
            cityName = name,
            region = region ?: "",
            regionCode = region_code ?: "",
            fullLabel = label,
            latitude = latitude,
            longitude = longitude
        )
    }
}