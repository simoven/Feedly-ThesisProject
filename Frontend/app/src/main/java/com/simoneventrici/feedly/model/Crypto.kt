package com.simoneventrici.feedly.model

data class Crypto(
    val ticker: String,
    val name: String,
    val statsId: String,
    val graphicsId: Int
) {
    fun getCryptoLogoUrl(): String {
        return "https://assets.coingecko.com/coins/images/$graphicsId/large/$statsId.png"
    }
}