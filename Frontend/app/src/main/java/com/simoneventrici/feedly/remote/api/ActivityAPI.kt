package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.model.RecentActivity
import com.simoneventrici.feedly.remote.dto.RecentActivityDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ActivityAPI {

    @GET("getUserRecentActivity")
    suspend fun getUserRecentActivity(
        @Header("Authorization") authToken: String
    ): List<RecentActivityDto>
}