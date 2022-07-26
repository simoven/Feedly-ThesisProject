package com.simoneventrici.feedly.commons

import com.simoneventrici.feedly.R

class ValidationException(val errorMsgCode: Int): Exception()

class StringValidator {
    companion object {

        @Throws(ValidationException::class)
        fun validate(min_len: Int, max_len: Int, regex: Regex, string: String, regexErrorMsgCode: Int? = null) {
            if(string.length < min_len)
                throw ValidationException(R.string.string_too_short)

            if(string.length > max_len)
                throw ValidationException(R.string.string_too_long)

            if(!string.matches(regex))
                throw ValidationException(regexErrorMsgCode ?: R.string.invalid_string)
        }
    }
}