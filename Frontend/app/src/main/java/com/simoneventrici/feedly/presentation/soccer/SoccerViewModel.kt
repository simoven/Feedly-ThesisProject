package com.simoneventrici.feedly.presentation.soccer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.LeagueStandings
import com.simoneventrici.feedly.model.SoccerLeague
import com.simoneventrici.feedly.model.SoccerTeam
import com.simoneventrici.feedly.model.TeamMatch
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.SoccerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoccerViewModel @Inject constructor(
    val soccerRepository: SoccerRepository,
    val preferences: DataStorePreferences
): ViewModel() {

    val allTeams = mutableStateOf<List<SoccerTeam>>(emptyList())
    val allLeagues = mutableStateOf<List<SoccerLeague>>(emptyList())
    val userFavouriteTeams = mutableStateOf<List<SoccerTeam>>(emptyList())
    val currentStandings = mutableStateOf<List<LeagueStandings>>(emptyList())
    val userFavouriteTeamsMatches = mutableStateOf<DataState<List<TeamMatch>>>(DataState.None())
    val userToken = mutableStateOf("")

    val matchesBoxHeight = mutableStateOf(0)
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    init {
        observeTokenChanges()
        getAllTeams()
        getCurrentStandings()
        getAllSoccerLeagues()
    }

    private fun observeTokenChanges() {
        preferences.tokensFlow.onEach { token ->
            userToken.value = token ?: ""

            if(token?.isNotBlank() == true)
                getUserFavouriteTeams()
        }.launchIn(viewModelScope)
    }


    private fun getAllTeams() {
        viewModelScope.launch {
            allTeams.value = soccerRepository.getAllTeams()
        }
    }

    private fun getAllSoccerLeagues() {
        viewModelScope.launch {
            allLeagues.value = soccerRepository.getAllSoccerLeagues()
        }
    }

    private fun getUserFavouriteTeams() {
        viewModelScope.launch {
            userFavouriteTeams.value = soccerRepository.getUserFavouriteTeams(userToken.value)
            getUserFavouriteTeamsMatches()
        }
    }

    fun getCurrentStandings() {
        preferences.favLeagueFlow.onEach { leagueId ->
            leagueId?.let { currentStandings.value = soccerRepository.getStandingsByLeagueId(it) }
        }.launchIn(viewModelScope)
    }

    fun saveFavouriteLeague(leagueId: Int) {
        viewModelScope.launch {
            preferences.saveFavouriteLeague(leagueId)
        }
    }

    fun setUserFavouritesTeams(teamIds: List<Int>) {
        viewModelScope.launch {
            if(soccerRepository.setUserFavouriteTeams(userToken.value, teamIds)) {
                getUserFavouriteTeams()
            }
        }
    }

    fun getUserFavouriteTeamsMatches() {
        soccerRepository.getUserFavouriteTeamMatches(userToken.value).onEach {
            userFavouriteTeamsMatches.value = it
        }.launchIn(viewModelScope)
    }

    fun dividerScrolled(isUp: Boolean) {
        _scrollUp.value = isUp
    }

}