package com.simoneventrici.feedlyBackend.model.primitives

import com.simoneventrici.feedlyBackend.util.StringValidator
import kotlin.jvm.Throws

class Username(val username: String) {
    private val MIN_LENGTH = 4
    private val MAX_LENGTH = 30
    private val regex = Regex("[A-Za-z0-9_]*")

    init {
        StringValidator.validate(MIN_LENGTH, MAX_LENGTH, regex, username)
    }

    override fun toString(): String {
        return username
    }
}