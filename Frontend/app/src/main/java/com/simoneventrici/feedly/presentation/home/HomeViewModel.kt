package com.simoneventrici.feedly.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.RecentActivity
import com.simoneventrici.feedly.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
): ViewModel() {

    val recentActivity = mutableStateOf<DataState<List<RecentActivity>>>(DataState.None())

    init {
        getUserRecentActivity(Constants.TEST_TOKEN)
    }

    fun getUserRecentActivity(authToken: String) {
        activityRepository.getUserRecentActivity(authToken).onEach {
            recentActivity.value = it
        }.launchIn(viewModelScope)
    }
}