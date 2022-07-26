package com.simoneventrici.feedly.model.primitives

import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.StringValidator

class Username(val value: String) {
    private val MIN_LENGTH = 4
    private val MAX_LENGTH = 30
    private val regex = Regex("[A-Za-z0-9_]*")

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, regex, value, R.string.invalid_username)
    }

    override fun toString(): String {
        return value
    }
}