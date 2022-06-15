package com.simoneventrici.feedlyBackend.datasource.dto.soccer

data class Fixture(
    val date: String,
    val id: Int,
    val referee: String,
    val status: Status,
    val timestamp: Int,
    val timezone: String
)