package com.simoneventrici.feedlyBackend.security

import java.security.SecureRandom
import java.util.*

object Util {
    private val secureRandom: SecureRandom = SecureRandom()
    private val base64Encoder: Base64.Encoder = Base64.getUrlEncoder()

    fun generateNewToken(): String {
        val randomBytes = ByteArray(30)
        secureRandom.nextBytes(randomBytes)
        return base64Encoder.encodeToString(randomBytes)
    }
}