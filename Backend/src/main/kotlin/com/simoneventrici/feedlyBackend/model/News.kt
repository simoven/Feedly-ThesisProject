package com.simoneventrici.feedlyBackend.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.ResultSet

data class News(
    private var id: Int,
    val author: String,
    val title: String,
    val description: String,
    @JsonProperty("news_url") val newsUrl: String,
    @JsonProperty("image_url") val imageUrl: String,
    @JsonProperty("source_name") val sourceName: String,
    @JsonProperty("source_id") val sourceId: String,
    @JsonProperty("published_date") val publishedDate: String,
    val keyword: String? = null,
    val category: Category?,
    var language: String = "en"
) {
    private var idEditCount = 0

    sealed class Category(val value: String) {
        class Business: Category("business")
        class General: Category("general")
        class Entertainment: Category("entertainment")
        class Health: Category("health")
        class Science: Category("science")
        class Sport: Category("sport")
        class Technology: Category("technology")

        override fun toString(): String {
            return value
        }

        companion object {
            fun parse(str: String): Category {
                return when(str) {
                    "business" -> Business()
                    "general" -> General()
                    "entertainment" -> Entertainment()
                    "health" -> Health()
                    "science" -> Science()
                    "sport" -> Sport()
                    "technology" -> Technology()
                    else -> throw IllegalStateException("Invalid category")
                }
            }
        }

    }

    companion object {
        fun fromResultSet(rs: ResultSet): News {
            return News(
                id = rs.getInt("id"),
                author = rs.getString("author"),
                title = rs.getString("title"),
                description = rs.getString("description"),
                newsUrl = rs.getString("news_url"),
                imageUrl = rs.getString("image_url"),
                sourceName = rs.getString("source_name"),
                sourceId = rs.getString("source_id"),
                publishedDate = rs.getString("published_date"),
                category = rs.getString("category")?.let { Category.parse(it) },
                language = rs.getString("language")
            )
        }
    }

    fun getId(): Int = id
    fun setId(value: Int) {
        if(idEditCount == 0) {
            id = value
            ++idEditCount
        }
        else
            throw IllegalStateException("News id can't be modified twice")
    }
}