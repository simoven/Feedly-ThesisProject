package com.simoneventrici.feedlyBackend.service

import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

@Service
class ScheduledService(
    private val newsService: NewsService
) {

    // fetcho le news ogni 8 ore
    val INTERVAL_FETCHING_NEWS: Long = 1000 * 60 * 60 * 8
    // controllo le notizie vecchie ogni 25 ore
    val INTERVAL_REMOVE_NEWS: Long = 1000 * 60 * 60 * 25

    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    init {
        GlobalScope.launch(Dispatchers.IO) {
            checkAndFetchNews()
        }

        GlobalScope.launch(Dispatchers.Default) {
            removeOldNews()
        }
    }

    suspend fun checkAndFetchNews() {
        delay(4000)
        while(coroutineContext.isActive) {
            kotlin.runCatching {
                /*println("${dateFormat.format(Date())} REQUESTING Fetch News by category")
                newsService.fetchAllNewsByCategory()
                println("${dateFormat.format(Date())} FETCHED News by category")*/
            }.onFailure {
                println("${it::class} : ${it.message}")
            }
            delay(INTERVAL_FETCHING_NEWS)
        }
    }

    suspend fun removeOldNews() {
        while(coroutineContext.isActive) {
            newsService.removeOldNews()
            println("${dateFormat.format(Date())} REMOVED Old news")
            delay(INTERVAL_REMOVE_NEWS)
        }
    }
}