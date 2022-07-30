package com.simoneventrici.feedly.presentation.explore

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.Emoji
import com.simoneventrici.feedly.model.NewsAndReactions
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val preferences: DataStorePreferences
): ViewModel() {

    // contengono tutti i dati relativi alle notizie, se sono stati fetchate, se ci sono stati errori ecc
    val currentNewsByCategory = mutableStateOf<Map<String, DataState<List<NewsAndReactions>>>>(emptyMap())
    val favNewsLanguage = mutableStateOf("en")

    // questa mappa contiene, per ogni categoria di notizie, l'ultima notizia per cui è servito un aggiornamento
    // delle reaction in seguito all'inserimento da parte dell'utente di una reaction
    // serve per fare la ricomposizione delle pagine
    val latestNewsIdReactionAddedByCategory = mutableStateOf<Map <String, Pair<Int, String>>>(emptyMap())
    val userToken = mutableStateOf("")
    val isRefreshing = mutableStateOf(false)

    private var lastScrollIndex = 0
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    init {
        observeTokenChanges()
        observeNewsLanguages()
    }

    private fun observeTokenChanges() {
        preferences.tokensFlow.onEach { token ->
            userToken.value = token ?: ""

            if(token?.isNotBlank() == true)
                currentNewsByCategory.value = emptyMap()

        }.launchIn(viewModelScope)
    }

    private fun observeNewsLanguages() {
        preferences.newsLangFlow.onEach { language ->
            if(language != favNewsLanguage.value) {
                favNewsLanguage.value = language
                currentNewsByCategory.value = emptyMap()
            }
        }.launchIn(viewModelScope)
    }

    fun refreshNews(category: NewsCategory) {
        getNewsByCategory(userToken.value, category = category)
        isRefreshing.value = false
    }

    fun saveNewsLanguage(lang: String) {
        viewModelScope.launch {
            preferences.saveNewsLanguage(lang)
        }
    }

    fun getNewsByCategory(token: String, category: NewsCategory) {
        newsRepository.getNewsByCategory(
            authToken = token,
            category = category,
            language = favNewsLanguage.value
        ).onEach {
            val currentMap = currentNewsByCategory.value.toMutableMap()
            currentMap[category.value] = it
            currentNewsByCategory.value = currentMap
        }.launchIn(viewModelScope)
    }

    private fun getNewsInMap(newsId: Int, category: String): NewsAndReactions? {
        return currentNewsByCategory.value[category]?.data?.firstOrNull() {
            it.news.id == newsId
        }
    }

    fun getAllNews(): List<NewsAndReactions> {
        // è una lista di liste contententi tutte le news per categoria
        val allNewsByCategory = currentNewsByCategory.value.values.map { it.data }
        // [0] -> list di general, [1] -> list di business ecc
        val allNewsList = mutableListOf<NewsAndReactions>()
        allNewsByCategory.forEach {
            allNewsList.addAll(it ?: emptyList())
        }
        return allNewsList
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
        }.launchIn(viewModelScope)
    }

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }
}