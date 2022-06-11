package com.simoneventrici.feedlyBackend.datasource.dto.news

import com.simoneventrici.feedlyBackend.model.News

data class NewsDto(
    val author: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source,
    val title: String,
    val url: String?,
    val urlToImage: String?
) {
    fun toNews(category: String?, keyword: String?): News {
        return News(
            id = -1,
            author = author ?: "No author",
            title = title,
            description = description ?: "No description",
            newsUrl = url ?: "No url",
            imageUrl = urlToImage ?: "No image url",
            sourceId = source.id ?: "No source id",
            sourceName = source.name ?: "No source",
            publishedDate = publishedAt ?: "No date",
            category = if(category != null) News.Category.parse(category) else null
        )
    }
}