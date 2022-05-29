package com.simoneventrici.feedly.model

data class NewsAndReactions(
    val news: News,
    var reactions: Map<String, Int>,
    var userReaction: String
) {
}