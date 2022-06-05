package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.CryptoDto
import retrofit2.http.GET
import retrofit2.http.Header

interface CryptoAPI {

    @GET("userFavourites")
    suspend fun getUserFavouritesCrypto(
        @Header("Authorization") authToken: String
    ): List<CryptoDto>
}