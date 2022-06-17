package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.LeagueStandings
import com.simoneventrici.feedly.model.NewsAndReactions
import com.simoneventrici.feedly.model.SoccerTeam
import com.simoneventrici.feedly.model.TeamMatch
import com.simoneventrici.feedly.remote.api.SoccerAPI
import com.simoneventrici.feedly.remote.dto.AddReactionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class SoccerRepository(
    val soccerAPI: SoccerAPI,
    val context: Context
) {

    suspend fun getAllTeams(): List<SoccerTeam> {
        return try {
            soccerAPI.getAllTeams().map { it.toSoccerTeam() }
        } catch(e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserFavouriteTeams(authToken: String): List<SoccerTeam> {
        return try {
            soccerAPI.getUserFavouriteTeams(authToken).map { it.toSoccerTeam() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getStandingsByLeagueId(leagueId: Int): List<LeagueStandings> {
        return try {
            soccerAPI.getStandingsByLeagueId(leagueId).map { it.toLeagueStandings() }
        } catch(e: Exception) {
            emptyList()
        }
    }

    fun getUserFavouriteTeamMatches(authToken: String): Flow<DataState<List<TeamMatch>>> = flow {
        try {
            emit(DataState.Loading<List<TeamMatch>>())
            val result = soccerAPI.getUserFavouriteTeamsMatches(authToken).map { it.toTeamMatch() }
            emit(DataState.Success(result))
        } catch(e: HttpException) {
            emit(DataState.Error<List<TeamMatch>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<TeamMatch>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    suspend fun setUserFavouriteTeams(authToken: String, favTeams: List<Int>): Boolean {
        val arr = JSONArray().apply { favTeams.forEach { put(it) } }
        val body = JSONObject().apply {
            put("teamIds", arr)
        }
        val bodyJson = body.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = soccerAPI.setUserFavouriteTeams(authToken, bodyJson)
        return response.isSuccessful
    }
}