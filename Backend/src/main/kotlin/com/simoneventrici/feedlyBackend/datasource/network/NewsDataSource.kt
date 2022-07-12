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
        response?.body?.let { newsListDto ->
            articles = newsListDto.articles
                .filter { it.urlToImage != null }
                .map { it.toNews(category.value, null) }
                .onEach { it.language = countryToLanguage(country) }
        }

        return if (articles == null) State(errorMsg = "Error while fetching news (${response?.statusCode})", data = null)
        else State(data = articles)
    }
}