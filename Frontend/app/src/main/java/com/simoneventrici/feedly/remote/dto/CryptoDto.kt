package com.simoneventrici.feedly.remote.dto

import com.simoneventrici.feedly.model.Crypto

class CryptoDto(
    val ticker: String,
    val name: String,
    val stats_id: String,
    val graphic_id: Int
) {
    fun toCrypto(): Crypto {
        return Crypto(
            ticker = ticker,
            name = name,
            statsId = stats_id,
            graphicsId = graphic_id
        )
    }
}