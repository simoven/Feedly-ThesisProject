package com.simoneventrici.feedly.presentation.weather

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.GeoLocalizationInfo
import com.simoneventrici.feedly.model.WeatherOverview
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
    val weatherRepository: WeatherRepository
): ViewModel() {

    val geoLocalizationStatus = mutableStateOf<GeoLocalizationInfo?>(null)
    val currentWeatherStatus = mutableStateOf<DataState<WeatherOverview>>(DataState.None())

    init {
        viewModelScope.launch {
            geoLocalizationStatus.value = geoLocalizationRepository.getGeolocalizationDataFromAddress("Rende CS")[0]
            fetchWeatherInfo()
        }
    }

    private fun fetchWeatherInfo() {
        geoLocalizationStatus.value?.let {
            weatherRepository.getWeatherInfoByCoords(it.latitude, it.longitude).onEach { state ->
                currentWeatherStatus.value = state
            }.launchIn(viewModelScope)
        }
    }
}