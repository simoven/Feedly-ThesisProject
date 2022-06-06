package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.Crypto

class CryptoDto(
    val ticker: String,
    val name: String,
    val stats_id: String,
    val image_url: String
) {
    fun toCrypto(): Crypto {
        return Crypto(
            ticker = ticker,
            name = name,
            statsId = stats_id,
            imageUrl = image_url
        )
    }
}