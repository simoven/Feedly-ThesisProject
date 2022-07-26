package com.simoneventrici.feedlyBackend.service

import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

@Service
class ScheduledService(
    private val newsService: NewsService,
    private val soccerService: SoccerService,
    private val stockService: StockService
) {

    // fetcho le news ogni 8 ore
    val INTERVAL_FETCHING_NEWS: Long = 1000 * 60 * 60 * 8
    // controllo le notizie vecchie ogni 25 ore
    val INTERVAL_REMOVE_NEWS: Long = 1000 * 60 * 60 * 25
    // dati del calcio ogni 12 ore
    val INTERVAL_FETCH_SOCCER_DATA: Long = 1000 * 60 * 60 * 12
    // finanza ogni 2 ore
    val INTERVAL_FETCH_STOCKS: Long = 1000 * 60 * 60 * 2

    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    init {
        //GlobalScope.launch(Dispatchers.IO) { checkAndFetchNews() }
        //GlobalScope.launch(Dispatchers.IO) { fetchSoccerData() }
        //GlobalScope.launch(Dispatchers.Default) { removeOldNews() }
        //GlobalScope.launch(Dispatchers.Default) { fetchFinanceData() }
    }

    suspend fun checkAndFetchNews() {
        delay(4000)
        while(coroutineContext.isActive) {
            kotlin.runCatching {
                println("${dateFormat.format(Date())} REQUESTING Fetch News by category")
                newsService.fetchAllNewsByCategory()
                println("${dateFormat.format(Date())} FETCHED News by category")
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

    suspend fun fetchSoccerData() {
        delay(2000)
        while(coroutineContext.isActive) {
            kotlin.runCatching {
                soccerService.fetchAllTeamMatches()
                println("${dateFormat.format(Date())} FETCHED Team Matches")
                soccerService.fetchAllLeaguesStandings()
                println("${dateFormat.format(Date())} FETCHED League Standings")
            }.onFailure {
                println("${it::class} : ${it.message}")
            }
            delay(INTERVAL_FETCH_SOCCER_DATA)
        }
    }

    suspend fun fetchFinanceData() {
        delay(2000)
        //while(coroutineContext.isActive) {
            kotlin.runCatching {
                stockService.fetchMarketData()
                println("${dateFormat.format(Date())} FETCHED Finance Data")
            }.onFailure {
                println("${it::class} : ${it.message}")
            }
            delay(INTERVAL_FETCH_STOCKS)
        //}
    }
}