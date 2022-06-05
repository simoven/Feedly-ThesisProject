package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.RecentActivity
import com.simoneventrici.feedly.remote.api.ActivityAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ActivityRepository constructor(
    private val activityAPI: ActivityAPI,
    private val context: Context
) {

    fun getUserRecentActivity(authToken: String): Flow<DataState<List<RecentActivity>>> = flow {
        try {
            emit(DataState.Loading<List<RecentActivity>>())
            val result = activityAPI.getUserRecentActivity(authToken).map { it.toRecentActivity() }
            println(result)
            emit(DataState.Success(data = result))
        } catch(e: HttpException) {
            emit(DataState.Error<List<RecentActivity>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<RecentActivity>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }
}