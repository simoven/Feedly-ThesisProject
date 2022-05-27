package com.simoneventrici.feedlyBackend.datasource.dto.news

import com.simoneventrici.feedlyBackend.model.News

data class NewsAndReactionsDto(
    val news: News,
    val reactions: Map<String, Int>,
    val userReaction: String?
)
