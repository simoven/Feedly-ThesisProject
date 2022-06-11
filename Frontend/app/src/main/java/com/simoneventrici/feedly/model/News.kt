package com.simoneventrici.feedly.model

import com.simoneventrici.feedly.model.primitives.NewsCategory

data class News(
    val id: Int,
    val author: String,
    val title: String,
    val description: String,
    val newsUrl: String,
    val imageUrl: String,
    val sourceName: String,
    val sourceId: String,
    val publishedDate: String,
    val category: NewsCategory?,
    val language: String = "en",
)