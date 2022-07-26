package com.simoneventrici.feedly.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.RecentActivity
import com.simoneventrici.feedly.model.primitives.NewsCategory
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val preferences: DataStorePreferences
): ViewModel() {

    val recentActivity = mutableStateOf<DataState<List<RecentActivity>>>(DataState.None())
    val userToken = mutableStateOf("")

    init {
        observeTokenChanges()
    }

    private fun observeTokenChanges() {
        preferences.tokensFlow.onEach { token ->
            userToken.value = token ?: ""

            if(token?.isNotBlank() == true)
                getUserRecentActivity(userToken.value)
        }.launchIn(viewModelScope)
    }


    fun getUserRecentActivity(authToken: String) {
        activityRepository.getUserRecentActivity(authToken).onEach {
            recentActivity.value = it
        }.launchIn(viewModelScope)
    }
}