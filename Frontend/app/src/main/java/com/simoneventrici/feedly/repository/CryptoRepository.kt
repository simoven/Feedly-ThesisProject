package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.CryptoMarketData
import com.simoneventrici.feedly.remote.api.CoingeckoAPI
import com.simoneventrici.feedly.remote.api.CryptoAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class CryptoRepository(
    private val cryptoAPI: CryptoAPI,
    private val context: Context,
    private val coingeckoAPI: CoingeckoAPI
)  {

    fun getUserFavouritesCrypto(authToken: String): Flow<DataState<List<Crypto>>> = flow {
        try {
            emit(DataState.Loading<List<Crypto>>())
            val cryptos = cryptoAPI.getUserFavouritesCrypto(authToken).map { it.toCrypto() }
            emit(DataState.Success(data = cryptos))
        } catch(e: HttpException) {
            emit(DataState.Error<List<Crypto>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<Crypto>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    fun getCryptoMarketData(cryptos: List<String>): Flow<DataState<List<CryptoMarketData>>> = flow {
        try {
            val cryptoIds = cryptos.joinToString(",")
            println("IDS: --$cryptoIds--")
            emit(DataState.Loading<List<CryptoMarketData>>())
            val cryptoResult = coingeckoAPI.getCoinsMarketData(cryptoIds).map { it.toCryptoMarketData() }
            println("RESULT $cryptoResult")
            emit(DataState.Success(data = cryptoResult))
        } catch(e: HttpException) {
            emit(DataState.Error<List<CryptoMarketData>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<CryptoMarketData>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }
}