package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.math.roundToInt

data class StocksData(
    val name: String,
    val ticker: String,
    val prices: List<Double>,
    @JsonProperty("latest_close") val latestClose: Double,
    @JsonProperty("change_24h") var change24h: Double
) {
    init {
        // i prezzi sono su 5 giorni, e prendo il prezzo alla fine del 4°, quindi 5/4 = 0.8
        val oldPrice24h = prices[(prices.size * 0.8f).roundToInt()]
        val latestPrice = prices.last()
        change24h = (latestPrice - oldPrice24h) / oldPrice24h * 100
    }
}