package com.simoneventrici.feedlyBackend.util

import kotlin.jvm.Throws

class StringValidator {
    companion object {
        @Throws(IllegalStateException::class)
        fun validate(min_len: Int, max_len: Int, regex: Regex, string: String) {
            if(string.length !in min_len..max_len)
                throw IllegalStateException("String length not compliant with min/max length")

            if(!string.matches(regex))
                throw IllegalStateException("String doesn't matches pattern")
        }
    }
}