package com.simoneventrici.feedlyBackend.model

import com.simoneventrici.feedlyBackend.model.primitives.Email
import com.simoneventrici.feedlyBackend.model.primitives.Username

data class User(
    private val username: Username,
    private val email: Email
)