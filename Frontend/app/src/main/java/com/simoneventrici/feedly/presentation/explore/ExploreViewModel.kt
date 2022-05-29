package com.simoneventrici.feedly.presentation.explore

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.News
import com.simoneventrici.feedly.model.NewsAndReactions
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    val currentNewsByCategory = mutableStateOf<DataState<List<NewsAndReactions>>>(DataState.None())
    val currentNewsByKeyword = mutableStateOf<DataState<List<News>>>(DataState.None())

    init {
        getNewsByCategory(Constants.TEST_TOKEN, NewsCategory.Business(), "en")
    }

    fun getNewsByCategory(token: String, category: NewsCategory, language: String) {
        newsRepository.getNewsByCategory(
            authToken = token,
            category = category,
            language = language
        ).onEach {
            currentNewsByCategory.value = it
        }.launchIn(viewModelScope)
    }

    fun getNewsByKeyword(keyword: String) {
        newsRepository.getNewsByKeyword(
            keyword = keyword
        ).onEach {
            currentNewsByKeyword.value = it
        }.launchIn(viewModelScope)
    }
}