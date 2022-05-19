package com.simoneventrici.feedlyBackend.model.primitives

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UsernameTest {

    @Test
    fun shouldFailLength() {
        val values = listOf("as", "", "cio", "nrrnvornvroinvoirvnrorvrvrvrvrvrvrvrrinoi")
        values.forEach { assertThrows(IllegalStateException::class.java) { Username(it) } }
    }

    @Test
    fun shouldFailRegex() {
        val values = listOf("ciao com", "simo@ne", "*nonciso?", "<username>", "invalid-one")
        values.forEach { assertThrows(IllegalStateException::class.java) { Username(it) } }
    }

    @Test
    fun shouldPassValidation() {
        val values = listOf("simone", "user_name", "name80user18", "_valid_name", "spring_boot_12_kotlin")
        values.forEach { Username(it) }
    }
}