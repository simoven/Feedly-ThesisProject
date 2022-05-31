package com.simoneventrici.feedlyBackend.datasource.api

import com.simoneventrici.feedlyBackend.datasource.dto.news.NewsListDto
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.util.Properties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Component
class NewsAPI(
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val appProperties: Properties
) {
    private val BASE_URL = "https://newsapi.org/v2/"

    companion object {
        val SORT_POPULARITY = "popularity"
        val SORT_RELEVANCY = "relevancy"
        val SORT_NEWEST = "publishedAt"
    }

    private fun getUrl(path: String, category: String? = null, country: String? = null, language: String? = null,
                       keyword: String? = null, sortBy: String? = null, pageSize: String = "20"): String {
        val keywordPath = if(keyword != null) "&q=$keyword" else ""
        val sortByPath = if(sortBy != null) "&sortBy=$sortBy" else ""
        val categoryPath = if(category != null) "&category=$category" else ""
        val countryPath = if(country != null) "&country=$country" else ""
        val languagePath = if(language != null) "&language=$language" else ""
        return "$BASE_URL/$path?pageSize=$pageSize&apiKey=${appProperties.newsApiKey}$categoryPath$keywordPath$sortByPath$countryPath$languagePath"
    }

    fun getNewsByCategory(category: News.Category, country: String): ResponseEntity<NewsListDto> {
        return restTemplate.getForEntity(
            getUrl(
                path = "top-headlines",
                category = category.value,
                country = country,
            )
        )
    }

    fun getNewsByKeyword(keyword: String, language: String, sortBy: String): ResponseEntity<NewsListDto> {
        return restTemplate.getForEntity(
            getUrl(
                path = "everything",
                keyword = keyword,
                language = language,
                sortBy = sortBy,
                pageSize = "15"
            )
        )
    }

}