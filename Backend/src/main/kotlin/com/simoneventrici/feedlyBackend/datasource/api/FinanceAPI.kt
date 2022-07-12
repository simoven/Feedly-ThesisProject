package com.simoneventrici.feedlyBackend.datasource.api

import com.simoneventrici.feedlyBackend.datasource.dto.stocks.StocksDataDto
import com.simoneventrici.feedlyBackend.util.Properties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.text.SimpleDateFormat
import java.util.*

@Component
class FinanceAPI(
    @Autowired private val restTemplate: RestTemplate,
    @Autowired private val appProperties: Properties
) {
    private val BASE_URL = "https://yfapi.net/v8/finance"

    fun getMarketInfoByTicker(tickers: List<String>): ResponseEntity<Map<String, StocksDataDto>?>? {
        val tickerString = tickers.joinToString("%2C") { it.uppercase() }
        val url = "$BASE_URL/spark?interval=15m&range=5d&symbols=$tickerString"
        val headers = HttpHeaders().apply { set("X-API-KEY", appProperties.financeApiKey) }

        var result: ResponseEntity<Map<String, StocksDataDto>?>? = null
        kotlin.runCatching {
            result = restTemplate.exchange(url, HttpMethod.GET, HttpEntity<String>(headers))
        }.onFailure {
            println(SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date()) +  it.message)
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        return result
    }
}