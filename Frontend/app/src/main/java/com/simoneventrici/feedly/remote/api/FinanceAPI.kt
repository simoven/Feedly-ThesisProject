package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.CryptoDto
import com.simoneventrici.feedly.remote.dto.StockDataDto
import com.simoneventrici.feedly.remote.dto.StockDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FinanceAPI {

    @GET("userFavourites")
    suspend fun getUserFavouritesStocks(
        @Header("Authorization") authToken: String
    ): List<StockDataDto>

    @GET("all")
    suspend fun getAllStocks(): List<StockDto>

    @POST("addFavouriteStock")
    suspend fun addFavouriteStock(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @POST("removeFavouriteStock")
    suspend fun removeFavouriteStock(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}