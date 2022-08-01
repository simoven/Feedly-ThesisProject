package com.simoneventrici.feedly.remote.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("login")
    suspend fun doCredentialsLogin(@Body body: RequestBody): Response<ResponseBody>

    @POST("tokenLogin")
    suspend fun doTokenLogin(@Body body: RequestBody): Response<ResponseBody>

    @POST("register")
    suspend fun doRegistration(@Body body: RequestBody): Response<ResponseBody>

    @POST("changeUserPassword")
    suspend fun doUserPasswordChange(@Body body: RequestBody): Response<ResponseBody>

    @POST("logout")
    suspend fun doUserLogout(@Body body: RequestBody)

    @POST("googleLogin")
    suspend fun doGoogleLogin(@Body body: RequestBody): Response<ResponseBody>

    @POST("forgotPassword")
    suspend fun resetPassword(@Body body: RequestBody): Response<ResponseBody>

}