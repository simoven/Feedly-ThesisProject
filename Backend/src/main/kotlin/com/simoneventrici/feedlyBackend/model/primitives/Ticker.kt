package com.simoneventrici.feedlyBackend.model.primitives

import com.simoneventrici.feedlyBackend.security.StringValidator

data class Ticker(val value: String) {
    private val MIN_LENGTH = 2
    private val MAX_LENGTH = 20
    private val regex = Regex("[A-Za-z]*")

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, regex, value, "The provided ticker is not valid")
    }

    override fun toString(): String {
        return value
    }
}