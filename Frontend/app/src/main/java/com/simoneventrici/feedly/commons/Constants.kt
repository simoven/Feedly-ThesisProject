package com.simoneventrici.feedly.commons

import android.content.Context
import android.content.pm.PackageManager

class Constants(
   val context: Context
) {
    companion object {
        const val FEEDLY_BACKEND_URL = "http://192.168.1.14:8080"
        const val COINRANKING_URL = "https://api.coinranking.com/v2/"
        const val COINGECKO_URL = "https://api.coingecko.com/api/v3/"
        // only for test purposes
        const val TEST_TOKEN = "sOceJk56q9xuPyR9OYjIah9fGK-3goWb-VR4LZDH"
    }

    val coinrankingApiKey = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData["coinrankingKey"]
}