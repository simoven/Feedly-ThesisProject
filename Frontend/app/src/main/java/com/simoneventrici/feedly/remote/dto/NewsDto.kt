package com.simoneventrici.feedly.remote.dto

import com.google.gson.annotations.SerializedName
import com.simoneventrici.feedly.model.News
import com.simoneventrici.feedly.model.primitives.NewsCategory
import org.json.JSONObject

data class NewsDto(
    val id: Int,
    val author: String,
    val title: String,
    val description: String,
    @SerializedName("news_url") val newsUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("source_name") val sourceName: String,
    @SerializedName("source_id") val sourceId: String,
    @SerializedName("published_date") val publishedDate: String,
    val category: CategoryDto?,
    val language: String = "en"
) {
    fun toNews(): News {
        return News(
            id = id,
            author = author,
            title = title,
            description = description,
            newsUrl = newsUrl,
            imageUrl = imageUrl,
            sourceName = sourceName,
            sourceId = sourceId,
            publishedDate = publishedDate,
            category = category?.let { NewsCategory.parse(it.value) },
            language = language
        )
    }
}