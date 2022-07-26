package com.simoneventrici.feedly.model.primitives

import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.StringValidator

class Email(val value: String) {
    private val MIN_LENGTH = 6
    private val MAX_LENGTH = 50
    private val regex = Regex("[A-Za-z0-9\\.]*@[a-z]*\\.[a-z]*")

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, regex, value, R.string.invalid_email)
    }

    override fun toString(): String {
        return value
    }
}