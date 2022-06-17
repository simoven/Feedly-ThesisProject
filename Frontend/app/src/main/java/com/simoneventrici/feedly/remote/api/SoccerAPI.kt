package com.simoneventrici.feedly.remote.api

import com.simoneventrici.feedly.remote.dto.LeagueStandingsDto
import com.simoneventrici.feedly.remote.dto.SoccerTeamDto
import com.simoneventrici.feedly.remote.dto.TeamMatchDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface SoccerAPI {

    @GET("allTeams")
    suspend fun getAllTeams(): List<SoccerTeamDto>

    @POST("setFavouriteTeams")
    suspend fun setUserFavouriteTeams(
        @Header("Authorization") authToken: String,
        @Body body: RequestBody
    ): Response<ResponseBody>

    @GET("userFavouriteTeamsMatches")
    suspend fun getUserFavouriteTeamsMatches(
        @Header("Authorization") authToken: String
    ): List<TeamMatchDto>

    @GET("userFavouriteTeams")
    suspend fun getUserFavouriteTeams(
        @Header("Authorization") authToken: String
    ): List<SoccerTeamDto>

    @GET("standingsByLeagueId")
    suspend fun getStandingsByLeagueId(
        @Query("leagueId") leagueId: Int
    ): List<LeagueStandingsDto>



}