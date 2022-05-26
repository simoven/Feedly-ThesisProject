package com.simoneventrici.feedlyBackend.datasource.network

import com.simoneventrici.feedlyBackend.datasource.api.NewsAPI
import com.simoneventrici.feedlyBackend.model.News
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class NewsDataSource(
    @Autowired private val newsAPi: NewsAPI
) {

    private fun countryToLanguage(country: String): String {
        return when(country) {
            "it" -> "it"
            "us" -> "en"
            "uk" -> "en"
            else -> "en"
        }
    }

    fun getNewsByCategory(category: News.Category, country: String): State<Collection<News>> {
        val response = newsAPi.getNewsByCategory(category, country)
        var articles: Collection<News>? = null
        if (response.body != null)
            articles = response.body?.articles?.map { it.toNews(category.value, null) }?.onEach { it.language = country }

        return if (articles == null) State(errorMsg = "Error while fetching news (${response.statusCode})", data = null)
        else State(data = articles)
    }

    fun getNewsByKeyword(keyword: String, language: String, sortBy: String): State<Collection<News>> {
        val response = newsAPi.getNewsByKeyword(keyword, language, sortBy)
        var articles: Collection<News>? = null
        if (response.body != null)
            articles = response.body?.articles?.map { it.toNews(null, keyword) }?.onEach { it.language = language }

        return if (articles == null) State(errorMsg = "Error while fetching news (${response.statusCode})", data = null)
        else State(data = articles)
    }
}