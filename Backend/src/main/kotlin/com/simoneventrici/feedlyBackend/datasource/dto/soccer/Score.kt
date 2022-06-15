package com.simoneventrici.feedlyBackend.datasource.dto.soccer

data class Score(
    val extratime: Extratime,
    val fulltime: Fulltime,
    val halftime: Halftime,
    val penalty: Penalty
)