package com.simoneventrici.feedlyBackend.model.primitives

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PasswordTest {

    @Test
    fun shouldFailRegex() {
        val values = listOf("simone", "Password", "Password123", "password123@", "PASSWORD123@", "Pass??()123")
        values.forEach { assertThrows(IllegalStateException::class.java) { Password(it) } }
    }

    @Test
    fun shouldPassValidation() {
        val values = listOf("Password123@", "Pass.word9901", "MyNameIsPassword12!", "....abcABC123", "2000@#!^Pass")
        values.forEach { Password(it) }
    }
}