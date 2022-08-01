package com.simoneventrici.feedlyBackend.security

import java.security.SecureRandom
import java.util.*

object Util {
    private val secureRandom: SecureRandom = SecureRandom()
    private val base64Encoder: Base64.Encoder = Base64.getUrlEncoder()

    private val letters = "abcdefghijklmnopqrstuvwxyz"
    private val characters = "@?#$%^&+=!."

    fun generateNewToken(): String {
        val randomBytes = ByteArray(30)
        secureRandom.nextBytes(randomBytes)
        return base64Encoder.encodeToString(randomBytes)
    }

    fun generateNewPassword(): String {
        val newPassword = mutableListOf<Char>()
        repeat(4) {
            newPassword.add(letters.random())
            newPassword.add(letters.random().uppercaseChar())
            newPassword.add((0..9).random().toString()[0])
            newPassword.add(characters.random())
        }

        newPassword.shuffle()
        return newPassword.joinToString("")
    }
}