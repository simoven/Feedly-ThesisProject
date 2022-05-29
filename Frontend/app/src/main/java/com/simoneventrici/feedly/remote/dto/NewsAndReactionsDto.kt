package com.simoneventrici.feedly.remote.dto

data class NewsAndReactionsDto(
    val news: NewsDto,
    val reactions: Map<String, Int>,
    val userReaction: String?
)
