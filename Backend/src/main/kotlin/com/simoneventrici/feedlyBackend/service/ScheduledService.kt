package com.simoneventrici.feedlyBackend.service

import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import kotlin.coroutines.coroutineContext

@Service
class ScheduledService(
    private val newsService: NewsService
) {

    // fetcho le news ogni 8 ore
    val INTERVAL_FETCHING_NEWS: Long = 1000 * 60 * 60 * 8

    init {
        GlobalScope.launch(Dispatchers.IO) {
            delay(6000)
            //checkAndFetchNews()
            println(Thread.currentThread().name)
        }
    }
    suspend fun checkAndFetchNews() {
        while(coroutineContext.isActive) {
            println("Fetching")
            kotlin.runCatching {
                newsService.fetchAllNewsByCategory()
                newsService.fetchAllNewsByKeyword()
            }.onFailure {
                println("${it::class} : ${it.message}")
            }
            delay(INTERVAL_FETCHING_NEWS)
        }
    }
}