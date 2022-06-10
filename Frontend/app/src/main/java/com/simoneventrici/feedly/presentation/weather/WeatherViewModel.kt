package com.simoneventrici.feedly.presentation.weather

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.GeoLocalizationInfo
import com.simoneventrici.feedly.model.WeatherOverview
import com.simoneventrici.feedly.persistence.DataStorePreferences
import com.simoneventrici.feedly.repository.GeoLocalizationRepository
import com.simoneventrici.feedly.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val geoLocalizationRepository: GeoLocalizationRepository,
    val weatherRepository: WeatherRepository,
    val preferences: DataStorePreferences
): ViewModel() {

    val geoLocalizationStatus = mutableStateOf<GeoLocalizationInfo?>(null)
    val currentWeatherStatus = mutableStateOf<DataState<WeatherOverview>>(DataState.None())
    var geoLocalizationInfoQueryResult = mutableStateOf<DataState<List<GeoLocalizationInfo>>>(DataState.None())

    init {
        loadGeoInfoFromStorage()
    }

    fun getGeolocalizationDataFromAddress(address: String) {
        geoLocalizationRepository.getGeolocalizationDataFromAddress(address).onEach {
            geoLocalizationInfoQueryResult.value = it
        }.launchIn(viewModelScope)
    }

    fun saveGeolocalizationAddress(geoLocalizationInfo: GeoLocalizationInfo) {
        viewModelScope.launch {
            preferences.saveGeoCoords(geoLocalizationInfo)
            geoLocalizationInfoQueryResult.value = DataState.None()
        }
    }

    private fun fetchWeatherInfo() {
        geoLocalizationStatus.value?.let {
            weatherRepository.getWeatherInfoByCoords(it.latitude, it.longitude).onEach { state ->
                currentWeatherStatus.value = state
            }.launchIn(viewModelScope)
        }
    }

    // carico i dati sulla posizione dallo storage e, se sono validi, fetcho i dati sul meteo
    private fun loadGeoInfoFromStorage() {
        preferences.geoInfoFlow.onEach {
            geoLocalizationStatus.value = it
            it?.let { fetchWeatherInfo() }
        }.launchIn(viewModelScope)
    }
}