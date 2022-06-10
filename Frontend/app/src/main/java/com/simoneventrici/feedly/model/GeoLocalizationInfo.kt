package com.simoneventrici.feedly.model

import java.lang.Exception

data class GeoLocalizationInfo(
    val cityName: String,
    val region: String,
    val regionCode: String,
    val fullLabel: String,
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "$cityName;$region;$regionCode;$fullLabel;$latitude;$longitude"
    }

    companion object {
        fun parseFromString(str: String): GeoLocalizationInfo? {
            return try {
                val strings = str.split(";")
                GeoLocalizationInfo(
                    cityName = strings[0],
                    region = strings[1],
                    regionCode = strings[2],
                    fullLabel = strings[3],
                    latitude = strings[4].toDouble(),
                    longitude = strings[5].toDouble()
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}