package com.simoneventrici.feedlyBackend.datasource.dto.soccer

data class LeagueX(
    val country: String,
    val flag: String,
    val id: Int,
    val logo: String,
    val name: String,
    val season: Int,
    val standings: List<List<Standing>>
)