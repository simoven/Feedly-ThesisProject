package com.simoneventrici.feedlyBackend.model.primitives

import com.simoneventrici.feedlyBackend.security.StringValidator

class Email(val value: String) {
    private val MIN_LENGTH = 6
    private val MAX_LENGTH = 50
    private val regex = Regex("[A-Za-z0-9\\.]*@[a-z]*\\.[a-z]*")

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, regex, value, "The provided email is not valid")
    }

    override fun toString(): String {
        return value
    }
}