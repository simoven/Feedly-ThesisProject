package com.simoneventrici.feedly.presentation.explore

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Emoji
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

    // contengono tutti i dati relativi alle ntozie, se sono stati fetchati, se ci sono stati errori ecc
    val currentNewsByCategory = mutableStateOf<Map<String, DataState<List<NewsAndReactions>>>>(emptyMap())
    val currentNewsByKeyword = mutableStateOf<DataState<List<News>>>(DataState.None())

    // questa mappa contiene, per ogni categoria di notizie, l'ultima notizia per cui è servito un aggiornamento
    // delle reaction in seguito all'inserimento da parte dell'utente di una reaction
    // serve per fare la ricomposizione delle pagine
    val latestNewsIdReactionAddedByCategory = mutableStateOf<Map <String, Pair<Int, String>>>(emptyMap())

    private var lastScrollIndex = 0
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    init {
        getNewsByCategory(Constants.TEST_TOKEN, NewsCategory.Business(), "en")
    }

    fun getNewsByCategory(token: String, category: NewsCategory, language: String) {
        newsRepository.getNewsByCategory(
            authToken = token,
            category = category,
            language = language
        ).onEach {
            val currentMap = currentNewsByCategory.value.toMutableMap()
            currentMap[category.value] = it
            currentNewsByCategory.value = currentMap
        }.launchIn(viewModelScope)
    }

    fun getNewsByKeyword(keyword: String) {
        newsRepository.getNewsByKeyword(
            keyword = keyword
        ).onEach {
            currentNewsByKeyword.value = it
        }.launchIn(viewModelScope)
    }

    private fun getNewsInMap(newsId: Int, category: String): NewsAndReactions? {
        return currentNewsByCategory.value[category]?.data?.firstOrNull() {
            it.news.id == newsId
        }
    }

    fun addReactionToNews(category: String, newsId: Int, emoji: Emoji, authToken: String ) {
        newsRepository.addReactionToNews(newsId = newsId, emojiCode = emoji.unicode_str, authToken = authToken).onEach { state ->
            if(state is DataState.Success) {
                val news = getNewsInMap(newsId, category)
                news?.let {
                    it.userReaction = state.data?.userReaction ?: ""
                    it.reactions = state.data?.newReactions ?: it.reactions
                }
                val currentMap = latestNewsIdReactionAddedByCategory.value.toMutableMap()
                currentMap[category] = Pair(newsId, news?.userReaction ?: "")
                latestNewsIdReactionAddedByCategory.value = currentMap
            }
            else {
                // TODO
                println("ERRORE NEWS REAZIONE: ${state.errorMsg}")
            }
        }.launchIn(viewModelScope)
    }

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }
}