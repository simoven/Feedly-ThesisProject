package com.simoneventrici.feedlyBackend.service

import com.simoneventrici.feedlyBackend.datasource.dao.CryptoDao
import com.simoneventrici.feedlyBackend.model.Crypto
import com.simoneventrici.feedlyBackend.model.primitives.Ticker
import com.simoneventrici.feedlyBackend.model.primitives.Username
import org.springframework.stereotype.Service

@Service
class CryptoService(
    val cryptoDao: CryptoDao
) {
    private val allCryptos = mutableMapOf<String, Crypto>()

    init {
        cryptoDao.getAll().onEach {
            allCryptos[it.ticker] = it
        }
    }

    fun getAllCryptos(): Collection<Crypto> {
        return allCryptos.values
    }

    fun getUserFavoritesCrypto(username: Username): Collection<Crypto> {
        return cryptoDao.getUserFavouritesCrypto(username.value).mapNotNull { ticker -> allCryptos[ticker] }
    }

    fun addCryptoFavourite(username: Username, ticker: Ticker) {
        cryptoDao.addFavouriteCrypto(username.value, ticker.value.lowercase())
    }

    fun removeCryptoFavourite(username: Username, ticker: Ticker) {
        cryptoDao.removeFavouriteCrypto(username.value, ticker.value.lowercase())
    }

    fun isCryptoSupported(ticker: Ticker): Boolean {
        return allCryptos[ticker.value.lowercase()] != null
    }
}