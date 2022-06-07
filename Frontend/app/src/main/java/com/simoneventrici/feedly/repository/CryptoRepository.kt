package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Crypto
import com.simoneventrici.feedly.model.CryptoMarketData
import com.simoneventrici.feedly.model.CryptoMarketStats
import com.simoneventrici.feedly.remote.api.CoingeckoAPI
import com.simoneventrici.feedly.remote.api.CoinrankingAPI
import com.simoneventrici.feedly.remote.api.CryptoAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class CryptoRepository(
    private val cryptoAPI: CryptoAPI,
    private val context: Context,
    private val coingeckoAPI: CoingeckoAPI,
    private val coinrankingAPI: CoinrankingAPI,
    private val constants: Constants
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
            emit(DataState.Loading<List<CryptoMarketData>>())
            val cryptoResult = coingeckoAPI.getCoinsMarketData(cryptoIds).map { it.toCryptoMarketData() }
            emit(DataState.Success(data = cryptoResult))
        } catch(e: HttpException) {
            emit(DataState.Error<List<CryptoMarketData>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<CryptoMarketData>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    fun getGlobalMarketStats(): Flow<DataState<CryptoMarketStats>> = flow {
        try {
            emit(DataState.Loading<CryptoMarketStats>())
            val result = coinrankingAPI.getMarketGlobalData(constants.coinrankingApiKey as String? ?: "").toMarketStats()
            emit(DataState.Success(data = result))
        } catch(e: HttpException) {
            emit(DataState.Error<CryptoMarketStats>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<CryptoMarketStats>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    suspend fun addCryptoToFavourite(authToken: String, ticker: String): Boolean {
        val body = JSONObject().apply {
            put("ticker", ticker)
        }
        val response = cryptoAPI.addCryptofavourite(authToken, body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
        return response.isSuccessful
    }

    suspend fun removeCryptoFromFavourite(authToken: String, ticker: String) {
        val body = JSONObject().apply {
            put("ticker", ticker)
        }
        cryptoAPI.removeCryptofavourite(authToken, body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
    }

    suspend fun getAllCryptos(): List<Crypto> {
        return try {
            cryptoAPI.getAllCryptos().map { it.toCrypto() }
        } catch(e: Exception) {
            emptyList()
        }
    }
}