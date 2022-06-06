package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.CryptoDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CryptoAPI {

    @GET("userFavourites")
    suspend fun getUserFavouritesCrypto(
        @Header("Authorization") authToken: String
    ): List<CryptoDto>

    @GET("all")
    suspend fun getAllCryptos(): List<CryptoDto>

    @POST("addFavourite")
    suspend fun addCryptofavourite(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): Response<ResponseBody>
}