package com.simoneventrici.feedlyBackend.model.primitives

import com.simoneventrici.feedlyBackend.security.StringValidator

class Username(val value: String) {
    private val MIN_LENGTH = 4
    private val MAX_LENGTH = 30
    private val regex = Regex("[A-Za-z0-9_]*")

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, regex, value, "The provided username is not valid")
    }

    override fun toString(): String {
        return value
    }
}