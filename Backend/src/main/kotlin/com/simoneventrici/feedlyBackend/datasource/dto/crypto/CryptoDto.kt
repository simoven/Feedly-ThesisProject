package com.simoneventrici.feedlyBackend.datasource.dto.crypto

import com.simoneventrici.feedlyBackend.model.Crypto

data class CryptoDto(
    val id: String,
    val image: String,
    val name: String,
    val symbol: String,
) {
    private fun getIdFromImageUrl(url: String): Int {
        val p1 = url.replace("https://assets.coingecko.com/coins/images/", "")
        val p2 = p1.replace("/.*".toRegex(), "")
        return p2.toInt()
    }

    fun toCrypto(): Crypto {
        return Crypto(
            ticker = symbol,
            statsId = id,
            name = name,
            graphicId = getIdFromImageUrl(image),
            imageUrl = image
        )
    }
}