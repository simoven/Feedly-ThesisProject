package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.datasource.dao.NewsDao
import com.simoneventrici.feedlyBackend.datasource.dto.news.NewsAndReactionsDto
import com.simoneventrici.feedlyBackend.datasource.dto.news.ReactionsDto
import com.simoneventrici.feedlyBackend.datasource.network.NewsDataSource
import com.simoneventrici.feedlyBackend.model.News
import com.simoneventrici.feedlyBackend.model.News.Category
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.model.primitives.Emoji
import org.springframework.stereotype.Service
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Service
class NewsService(
    private val newsDataSource: NewsDataSource,
    private val newsDao: NewsDao
) {

    // ogni fetch delle notizie richiede 7*2 = 14 API calls
    // limite giornaliero attuale = 100

    // le notizie per categoria vengono fetchate e salvate nel database
    // vengono poi aggiornate ogni tot ore

    private val allCategories = listOf(
        Category.Business(),
        Category.General(),
        Category.Entertainment(),
        Category.Health(),
        Category.Science(),
        Category.Sport(),
        Category.Technology())

    private val newsByCategory: MutableMap<String, MutableCollection<News>> = mutableMapOf()
    private val lock = ReentrantReadWriteLock(true)

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

            lock.write {
                newsByCategory[category.value]?.addAll(itNews.data ?: emptyList())
                newsByCategory[category.value]?.addAll(enNews.data ?: emptyList())
                newsByCategory[category.value]?.distinctBy { it.newsUrl }
            }

            itNews.data?.forEach { newsDao.save(it) }
            enNews.data?.forEach { newsDao.save(it) }
        }
    }

    // restituisco le notizie già fetchate in precedenza
    fun getAllCurrentNewsByCategory(category: Category, language: String, user: User): Collection<NewsAndReactionsDto> {
        lock.read {
            return newsByCategory[category.value]
                ?.filter { it.language == language }
                ?.sortedByDescending { it.publishedDate }
                ?.map {
                    // mappo ogni notizia all'oggetto che la contiene insieme alle reazioni
                    val reactions = newsDao.getNewsReactions(it.getId(), user)
                    NewsAndReactionsDto(
                        news = it,
                        reactions = reactions.newsReactions,
                        userReaction = reactions.userReaction
                    )
                } ?: emptyList()
        }
    }

    fun addReactionToNews(newsId: Int, user: User, emoji: Emoji): ReactionsDto {
        newsDao.addReactionToNews(newsId, user.getUsername(), emoji.unicode_str)
        return newsDao.getNewsReactions(newsId, user)
    }

    fun removeOldNews() {
        newsDao.removeOldNews()
    }
}