package com.simoneventrici.feedlyBackend.security

import kotlin.jvm.Throws

class StringValidator {
    companion object {
        @Throws(IllegalStateException::class)
        fun validate(min_len: Int, max_len: Int, regex: Regex, string: String, regexErrorMsg: String? = null) {
            if(string.length !in min_len..max_len)
                throw IllegalStateException("String length not in the min-max range")

            if(!string.matches(regex))
                throw IllegalStateException(regexErrorMsg ?: "String doesn't matches pattern")
        }
    }
}