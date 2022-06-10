package com.simoneventrici.feedly.remote.dto.weather

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") val rain_1h: Double
)