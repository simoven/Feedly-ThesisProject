package com.simoneventrici.feedlyBackend.model.primitives

import com.simoneventrici.feedlyBackend.util.StringValidator

class Password(val password: String) {
    private val MIN_LENGTH = 6
    private val MAX_LENGTH = 24
    private val pattern: String = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@?#$%^&+=!\\.])(?=\\S+$).{4,}$"

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, Regex(pattern), password)
    }

    override fun toString(): String {
        return password
    }
}