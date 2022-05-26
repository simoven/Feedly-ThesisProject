package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.datasource.api.NewsAPI
import com.simoneventrici.feedlyBackend.datasource.dao.NewsDao
import com.simoneventrici.feedlyBackend.datasource.network.NewsDataSource
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.model.News.Category
import org.springframework.stereotype.Service

@Service
class NewsService(
    private val newsDataSource: NewsDataSource,
    private val newsDao: NewsDao
) {

    val allKeywords = listOf(
        "elezioni", "sondaggi", "partiti",
        "ai", "robot", "spazio", "coding",
        "calcio", "basket", "tennis", "pallavolo",
        "gossip", "spettacolo",
    )
    private val allCategories = listOf(
        Category.Business(),
        Category.General(),
        /*Category.Entertainment(),
        Category.Health(),
        Category.Science(),
        Category.Sport(),
        Category.Technology()*/)

    private val newsByCategory: MutableMap<String, MutableCollection<News>> = mutableMapOf()
    private val newsByKeyword: MutableMap<String, MutableCollection<News>> = mutableMapOf()

    init {
        //Carico tutte le notizie dal database e le divido per keyword/categoria
        val allNews = newsDao.getAll()
        allCategories.forEach { category ->
            newsByCategory[category.value] = mutableListOf()
            newsByCategory[category.value]?.addAll(allNews.filter { news -> (news.category?.javaClass == category.javaClass) })
        }
        allKeywords.forEach { keyword ->
            newsByKeyword[keyword] = mutableListOf()
            newsByKeyword[keyword]?.addAll(allNews.filter { news -> news.keyword?.equals(keyword) ?: false })
        }
    }

    suspend fun fetchAllNewsByCategory() {
        allCategories.forEach { category ->
            val itNews = newsDataSource.getNewsByCategory(category, "it")
            val enNews = newsDataSource.getNewsByCategory(category, "us")
            newsByCategory[category.value]?.addAll(itNews.data ?: emptyList())
            newsByCategory[category.value]?.addAll(enNews.data ?: emptyList())
            newsByCategory[category.value]?.distinctBy { it.newsUrl }

            itNews.data?.forEach { newsDao.save(it) }
            enNews.data?.forEach { newsDao.save(it) }
        }
    }

    suspend fun fetchAllNewsByKeyword() {
        allKeywords.forEach { keyword ->
            val itNews = newsDataSource.getNewsByKeyword(keyword, "it", NewsAPI.SORT_RELEVANCY)
            newsByKeyword[keyword]?.addAll(itNews.data ?: emptyList())
            newsByKeyword[keyword]?.distinctBy { it.newsUrl }
            itNews.data?.forEach { newsDao.save(it) }
        }
    }

    // restrituisco le notizie già fetchate in precedenza
    fun getCurrentNewsByCategory(category: Category, language: String): Collection<News> {
        return newsByCategory[category.value]?.filter { it.language == language }?.sortedBy { it.publishedDate }?.reversed() ?: emptyList()
    }

    // le notizie per keyword sono solo in italiano
    fun getCurrentNewsByKeyword(keyword: String): Collection<News> {
        return newsByKeyword[keyword]?.sortedBy { it.publishedDate }?.reversed() ?: emptyList()
    }
}