package com.simoneventrici.feedly.repository

import android.content.Context
import com.google.gson.JsonObject
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.News
import com.simoneventrici.feedly.model.NewsAndReactions
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.remote.api.NewsAPI
import com.simoneventrici.feedly.remote.dto.AddReactionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsAPI: NewsAPI,
    private val context: Context
) {

    fun getNewsByCategory(authToken: String, category: NewsCategory, language: String): Flow<DataState<List<NewsAndReactions>>> = flow {
        try {
            emit(DataState.Loading<List<NewsAndReactions>>())
            val result = newsAPI.getNewsByCategory(authToken, category.value, language).map {
                NewsAndReactions(
                    news = it.news.toNews(),
                    reactions = it.reactions,
                    userReaction = it.userReaction ?: ""
                )
            }
            emit(DataState.Success(data = result))
        } catch(e: HttpException) {
            emit(DataState.Error<List<NewsAndReactions>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<NewsAndReactions>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    fun getNewsByKeyword(keyword: String): Flow<DataState<List<News>>> = flow {
        try {
            emit(DataState.Loading<List<News>>())
            val result = newsAPI.getNewsByKeyword(keyword).map { it.toNews() }
            emit(DataState.Success(data = result))
        } catch(e: HttpException) {
            emit(DataState.Error<List<News>>(e.localizedMessage ?: context.getString(R.string.unexpected_error_msg)))
        } catch(e: IOException) {
            emit(DataState.Error<List<News>>(context.getString(R.string.cannot_reach_server_msg)))
        }
    }

    fun addReactionToNews(newsId: Int, emojiCode: String, authToken: String): Flow<DataState<AddReactionResponse>> = flow {
        val body = JSONObject().apply {
            put("newsId", newsId)
            put("emoji", emojiCode)
        }
        val response = newsAPI.addReactionToNews(authToken, body.toString().toRequestBody("application/json".toMediaTypeOrNull()))
        val responseJson = JSONObject(response.body()?.string() ?: "{}")
        val responseJsonError = JSONObject(response.errorBody()?.string() ?: "{}")

        if(response.isSuccessful) {
            val reactionResp = AddReactionResponse(
                newsId = newsId,
                newReactions = (responseJson.get("news_reactions") as JSONObject).toMap(),
                userReaction = responseJson.get("user_reactions") as String?
            )
            emit(DataState.Success(data = reactionResp))
        }
        else {
            emit(DataState.Error<AddReactionResponse>(errorMsg = responseJsonError.get("msg") as String? ?: "Error while adding reaction"))
        }

    }

    private fun JSONObject.toMap(): Map<String, Int> {
        return try {
            val map = mutableMapOf<String, Int>()
            val keysItr: Iterator<String> = this.keys()
            while (keysItr.hasNext()) {
                val key = keysItr.next()
                var value = this.get(key) as Int
                map[key] = value
            }
            map
        } catch (e: Exception) {
            emptyMap()
        }
    }
}
