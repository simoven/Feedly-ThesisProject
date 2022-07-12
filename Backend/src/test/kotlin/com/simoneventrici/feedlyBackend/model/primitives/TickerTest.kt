package com.simoneventrici.feedlyBackend.model.primitives

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TickerTest {

    @Test
    fun shouldFailRegex() {
        val values = listOf("safe_moon", "verylongtickernotgood", "bitcoin@eth", "<btc>", "")
        values.forEach { assertThrows(IllegalStateException::class.java) { Ticker(it) } }
    }

    @Test
    fun shouldPassValidation() {
        val values = listOf("btc", "BTC", "SAFEMOOON", "LONGTICKERhere", "aave", "ticker123")
        values.forEach { Ticker(it) }
    }
}