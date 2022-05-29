package com.simoneventrici.feedly.repository

import android.content.Context
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.News
import com.simoneventrici.feedly.model.NewsAndReactions
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.remote.api.NewsAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
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
}
