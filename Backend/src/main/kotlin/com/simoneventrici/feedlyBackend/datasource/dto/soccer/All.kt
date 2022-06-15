package com.simoneventrici.feedlyBackend.datasource.dto.soccer

data class All(
    val draw: Int,
    val goals: GoalsX,
    val lose: Int,
    val played: Int,
    val win: Int
)