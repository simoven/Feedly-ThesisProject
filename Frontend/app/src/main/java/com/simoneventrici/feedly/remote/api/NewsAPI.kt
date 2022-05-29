package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.NewsAndReactionsDto
import com.simoneventrici.feedly.remote.dto.NewsDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface NewsAPI {

    @GET("newsByCategory")
    suspend fun getNewsByCategory(
        @Header("Authorization") authToken: String,
        @Query("category") category: String,
        @Query("language") language: String
    ): List<NewsAndReactionsDto>

    @POST("addReaction")
    suspend fun addReactionToNews(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @GET("newsByKeyword")
    suspend fun getNewsByKeyword(
        @Query("keyword") keyword: String
    ): List<NewsDto>
}