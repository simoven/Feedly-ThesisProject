package com.simoneventrici.feedlyBackend.datasource.dto.news

data class ReactionsDto(
    val newsReactions: Map<String, Int>,
    val userReaction: String?
)