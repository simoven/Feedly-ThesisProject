package com.simoneventrici.feedlyBackend.datasource.dto.soccer

import com.fasterxml.jackson.annotation.JsonProperty

data class GoalsX(
    val against: Int,
    @JsonProperty("for") val goalsFor: Int
)