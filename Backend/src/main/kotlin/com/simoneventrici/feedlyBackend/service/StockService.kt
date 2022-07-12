package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.datasource.api.FinanceAPI
import com.simoneventrici.feedlyBackend.datasource.dao.StockDao
import com.simoneventrici.feedlyBackend.model.Stock
import com.simoneventrici.feedlyBackend.model.StocksData
import com.simoneventrici.feedlyBackend.model.User
import com.simoneventrici.feedlyBackend.model.primitives.Ticker
import org.springframework.stereotype.Service

@Service
class StockService(
    val stockDao: StockDao,
    val financeAPI: FinanceAPI
) {
    val allStocks: List<Stock>
    private val stocksDataByTicker: MutableMap<String, StocksData> = mutableMapOf()

    init {
        allStocks = stockDao.getAll()
    }

    // fetcho i dati 10 ticker alla volta, per via dei limiti dell'API
    fun fetchMarketData() {
       /* var startIdx = 0
        var endIdx = startIdx + 10
        while(endIdx <= allStocks.size) {
            val subList = allStocks.subList(startIdx, endIdx)
            val result = financeAPI.getMarketInfoByTicker(subList.map { it.ticker })

            startIdx += 10
            endIdx += 10
        */
            val subList = listOf(
                Stock("aapl", "Apple"),
                Stock("msft", "Microsot"),
                Stock("goog", "Google"),
                Stock("baba", "Alibaba")
            )
            val result = financeAPI.getMarketInfoByTicker(subList.map { it.ticker })
            // per ogni ticker fetchato, aggiungo i dati di mercato alla mappa
            result?.body?.let { resultMap ->
                subList.forEach { stock ->
                    // resultMap è la mappa di marketData restituita dall'API
                    // prendo il Dto, lo converti in stockdata e lo aggiungo alla mappa
                    resultMap[stock.ticker.uppercase()]
                        ?.toStocksData(stock)
                        ?.let { stocksData -> stocksDataByTicker[stock.ticker] = stocksData }
                }
            }
        //}
    }

    fun isSupportedStock(ticker: Ticker): Boolean {
        return allStocks.any { it.ticker.lowercase() == ticker.value.lowercase() }
    }

    fun getUserFavouriteStocksData(user: User): List<StocksData> {
        val favStocks = stockDao.getUserFavouriteStocks(user)
        val stocksData = mutableListOf<StocksData>()

        favStocks.forEach { ticker ->
            stocksDataByTicker[ticker]?.let { stocksData.add(it) }
        }

        return stocksData
    }

    fun addUserFavouriteStock(user: User, stock: Ticker) {
        stockDao.addUserFavouriteStock(user, stock)
    }

    fun removeUserFavouriteStock(user: User, stock: Ticker) {
        stockDao.removeUserFavouriteStock(user, stock)
    }
}