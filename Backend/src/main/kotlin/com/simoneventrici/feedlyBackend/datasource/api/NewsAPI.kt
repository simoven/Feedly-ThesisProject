package com.simoneventrici.feedlyBackend.datasource.api

import com.simoneventrici.feedlyBackend.datasource.dto.news.NewsListDto
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.util.Properties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.text.SimpleDateFormat
import java.util.*

@Component
class NewsAPI(
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val appProperties: Properties
) {
    private val BASE_URL = "https://newsapi.org/v2/"

    private fun getUrl(path: String, category: String? = null, country: String? = null, language: String? = null,
                       keyword: String? = null, sortBy: String? = null, pageSize: String = "20"): String {
        val keywordPath = if(keyword != null) "&q=$keyword" else ""
        val sortByPath = if(sortBy != null) "&sortBy=$sortBy" else ""
        val categoryPath = if(category != null) "&category=$category" else ""
        val countryPath = if(country != null) "&country=$country" else ""
        val languagePath = if(language != null) "&language=$language" else ""
        return "$BASE_URL/$path?pageSize=$pageSize&apiKey=${appProperties.newsApiKey}$categoryPath$keywordPath$sortByPath$countryPath$languagePath"
    }

    fun getNewsByCategory(category: News.Category, country: String): ResponseEntity<NewsListDto>? {
        var result: ResponseEntity<NewsListDto>? = null
        kotlin.runCatching {
            result = restTemplate.getForEntity(
                getUrl(
                    path = "top-headlines",
                    category = category.value,
                    country = country,
                    pageSize = "25"
                )
            )
        }.onFailure {
            println(SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date()) +  it.message)
        }

        return result
    }
}