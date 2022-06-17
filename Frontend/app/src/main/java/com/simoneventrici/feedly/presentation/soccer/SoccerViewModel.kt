package com.simoneventrici.feedly.presentation.soccer

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.Constants
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.LeagueStandings
import com.simoneventrici.feedly.model.SoccerTeam
import com.simoneventrici.feedly.model.TeamMatch
import com.simoneventrici.feedly.repository.SoccerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoccerViewModel @Inject constructor(
    val soccerRepository: SoccerRepository
): ViewModel() {

    val allTeams = mutableStateOf<List<SoccerTeam>>(emptyList())
    val userFavouriteTeams = mutableStateOf<List<SoccerTeam>>(emptyList())
    val currentStandings = mutableStateOf<List<LeagueStandings>>(emptyList())
    val userFavouriteTeamsMatches = mutableStateOf<DataState<List<TeamMatch>>>(DataState.None())

    val matchesBoxHeight = mutableStateOf(0)
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    init {
        getAllTeams()
        getUserFavouriteTeams()
        getCurrentStandings()
    }

    private fun getAllTeams() {
        viewModelScope.launch {
            allTeams.value = soccerRepository.getAllTeams()
        }
    }

    private fun getUserFavouriteTeams() {
        viewModelScope.launch {
            userFavouriteTeams.value = soccerRepository.getUserFavouriteTeams(Constants.TEST_TOKEN)
            getUserFavouriteTeamsMatches()
        }
    }

    fun getCurrentStandings() {
        viewModelScope.launch {
            currentStandings.value = soccerRepository.getStandingsByLeagueId(135)
        }
    }

    fun setUserFavouritesTeams(teamIds: List<Int>) {
        viewModelScope.launch {
            if(soccerRepository.setUserFavouriteTeams(Constants.TEST_TOKEN, teamIds)) {
                getUserFavouriteTeams()
            }
        }
    }

    fun getUserFavouriteTeamsMatches() {
        soccerRepository.getUserFavouriteTeamMatches(Constants.TEST_TOKEN).onEach {
            userFavouriteTeamsMatches.value = it
        }.launchIn(viewModelScope)
    }

    fun dividerScrolled(isUp: Boolean) {
        _scrollUp.value = isUp
    }

}