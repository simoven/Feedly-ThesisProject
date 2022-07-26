package com.simoneventrici.feedly.commons

import android.content.Context
import android.content.pm.PackageManager

class Constants(
   val context: Context
) {
    companion object {
        const val FEEDLY_BACKEND_URL = "http://192.168.43.232:8080"
        const val COINRANKING_URL = "https://api.coinranking.com/v2/"
        const val COINGECKO_URL = "https://api.coingecko.com/api/v3/"
        const val WEATHER_API_URL = "https://api.openweathermap.org/data/3.0/"
        const val POSITIONSTACK_URL = "http://api.positionstack.com/v1/"
    }

    val coinrankingApiKey = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData["coinrankingApiKey"]
    val weatherApiKey = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData["weatherApiKey"]
    val positionStackApiKey = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData["positionStackApiKey"]
}