package com.simoneventrici.feedly.remote.dto.weather

import com.google.gson.annotations.SerializedName
import com.simoneventrici.feedly.remote.dto.DataX

data class GeoLocalizationInfoDto(
    @SerializedName("data") val geoData: List<DataX>
)