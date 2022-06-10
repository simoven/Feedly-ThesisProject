package com.simoneventrici.feedly.remote.dto.weather

import com.google.gson.annotations.SerializedName

data class GeoLocalizationInfoDto(
    @SerializedName("data") val geoData: List<DataX>
)