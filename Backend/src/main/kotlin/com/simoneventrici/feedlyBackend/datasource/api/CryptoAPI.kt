package com.simoneventrici.feedlyBackend.datasource.api

import com.simoneventrici.feedlyBackend.datasource.dao.CryptoDao
import com.simoneventrici.feedlyBackend.datasource.dto.crypto.CryptoDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

/* questa classe serve a fornire tutte le crypto al database, ma è un'operazione che va fatta una volta e basta,
    oppure in caso di aggiornamenti manuali
 */

//@Component
class CryptoAPI(
    @Autowired val restTemplate: RestTemplate,
    private val cryptoDao: CryptoDao
) {
    private val BASE_URL = "https://api.coingecko.com/api/v3"

    init {
        //getTopCryptos()
    }

    private fun getTopCryptos() {
        val response = restTemplate.getForEntity<Array<CryptoDto>>("$BASE_URL/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=200&page=1&sparkline=false")
        if(response.statusCode == HttpStatus.OK) {
            response.body?.forEach {
                cryptoDao.save(it.toCrypto())
            }
        }
    }
}