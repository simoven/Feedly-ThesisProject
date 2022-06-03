package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.datasource.api.NewsAPI
import com.simoneventrici.feedlyBackend.datasource.dao.NewsDao
import com.simoneventrici.feedlyBackend.datasource.dto.news.NewsAndReactionsDto
import com.simoneventrici.feedlyBackend.datasource.dto.news.ReactionsDto
import com.simoneventrici.feedlyBackend.datasource.network.NewsDataSource
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.model.News.Category
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.model.primitives.Emoji
import org.springframework.stereotype.Service

@Service
class NewsService(
    private val newsDataSource: NewsDataSource,
    private val newsDao: NewsDao
) {

    // ogni fetch delle notizie richiede 7*2 + 13 = 27 API calls
    // limite giornaliero attuale = 100

    // le notizie per categoria vengono fetchate e salvate nel database
    // quelle per keyword no, vengono caricate all'avvio del backend e tenute in memoria
    // vengono poi aggiornate ogni tot ore insieme a quelle per categoria

    val allKeywords = listOf(
        "elezioni", "sondaggi", "partiti",
        "ai", "robot", "spazio", "coding",
        "calcio", "basket", "tennis", "pallavolo",
        "gossip", "spettacolo",
    )
    private val allCategories = listOf(
        Category.Business(),
        Category.General(),
        Category.Entertainment(),
        Category.Health(),
        Category.Science(),
        Category.Sport(),
        Category.Technology())

    private val newsByCategory: MutableMap<String, MutableCollection<News>> = mutableMapOf()
    private val newsByKeyword: MutableMap<String, MutableCollection<News>> = mutableMapOf()

    init {
        //Carico tutte le notizie dal database e le divido per categoria
        val allNews = newsDao.getAll()
        allCategories.forEach { category ->
            newsByCategory[category.value] = mutableListOf()
            newsByCategory[category.value]?.addAll(allNews.filter { news -> (news.category?.javaClass == category.javaClass) })
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
        }
    }

    // restituisco le notizie già fetchate in precedenza
    fun getAllCurrentNewsByCategory(category: Category, language: String, user: User): Collection<NewsAndReactionsDto> {
        return newsByCategory[category.value]
            ?.filter { it.language == language }
            ?.sortedBy { it.publishedDate }
            ?.reversed()
            ?.map {
                // mappo ogni notizia all'oggetto che la contiene insieme alle reazioni
                val reactions = newsDao.getNewsReactions(it.getId(), user)
                NewsAndReactionsDto(news = it, reactions = reactions.newsReactions, userReaction = reactions.userReaction)
            } ?: emptyList()
    }

    // le notizie per keyword sono solo in italiano
    fun getAllCurrentNewsByKeyword(keyword: String): Collection<News> {
        return newsByKeyword[keyword]?.sortedBy { it.publishedDate }?.reversed() ?: emptyList()
    }

    fun addReactionToNews(newsId: Int, user: User, emoji: Emoji): ReactionsDto {
        newsDao.addReactionToNews(newsId, user.getUsername(), emoji.unicode_str)
        return newsDao.getNewsReactions(newsId, user)
    }

    fun removeOldNews() {
        newsDao.removeOldNews()
    }
}