package com.simoneventrici.feedly.remote.dto

data class AddReactionResponse(
    val newsId: Int,
    val newReactions: Map<String, Int>,
    val userReaction: String?
)
