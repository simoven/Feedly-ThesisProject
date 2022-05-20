package com.simoneventrici.feedlyBackend.model.primitives

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class EmailTest {

    @Test
    fun shouldFailRegex() {
        val values = listOf("<a>gmail.com", "?@gmail.com", "user.last@liveit", "hello-world@@domail.it.com", "email@gmail.com?")
        values.forEach { assertThrows(IllegalStateException::class.java) { Email(it) } }
    }

    @Test
    fun shouldPassValidation() {
        val values = listOf("a@b.com", "user.pass@live.it", "EMAIL23@outlook.com", "user@user.com")
        values.forEach { Email(it) }
    }
}