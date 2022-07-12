package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.*
import com.simoneventrici.feedly.remote.api.FinanceAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class StocksRepository(
    private val financeAPI: FinanceAPI,
    private val context: Context
) {
    fun getUserFavouritesStocks(authToken: String): Flow<DataState<List<StockData>>> = flow {
        try {
            emit(DataState.Loading<List<StockData>>())
            val stocks = financeAPI.getUserFavouritesStocks(authToken).map { it.toStockData() }
            emit(DataState.Success(data = stocks))
        } catch(e: HttpException) {
            emit(DataState.Error<List<StockData>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<StockData>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    suspend fun addStockToFavourite(authToken: String, ticker: String): Boolean {
        val body = JSONObject().apply {
            put("ticker", ticker)
        }
        val response = financeAPI.addFavouriteStock(authToken, body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
        return response.isSuccessful
    }

    suspend fun removeStockFromFavourite(authToken: String, ticker: String) {
        val body = JSONObject().apply {
            put("ticker", ticker)
        }
        financeAPI.removeFavouriteStock(authToken, body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
    }

    suspend fun getAllStocks(): List<Stock> {
        return try {
            financeAPI.getAllStocks().map { it.toStock() }
        } catch(e: Exception) {
            emptyList()
        }
    }
}