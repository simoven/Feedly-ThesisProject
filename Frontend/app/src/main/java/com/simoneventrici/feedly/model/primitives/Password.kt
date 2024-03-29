package com.simoneventrici.feedly.model.primitives

import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.StringValidator

class Password(val value: String) {
    private val MIN_LENGTH = 6
    private val MAX_LENGTH = 24
    private val pattern: String = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@?#$%^&+=!\\.])(?=\\S+$).{4,}$"

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, Regex(pattern), value, R.string.invalid_password)
    }

    override fun toString(): String {
        return value
    }
}