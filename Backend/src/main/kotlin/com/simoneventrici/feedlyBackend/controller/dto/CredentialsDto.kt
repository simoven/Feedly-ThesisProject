package com.simoneventrici.feedlyBackend.controller.dto

import com.simoneventrici.feedlyBackend.model.primitives.Username

data class CredentialsDto(
    val username: String,
    val password: String,
    val email: String?
)