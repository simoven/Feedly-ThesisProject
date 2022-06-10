package com.simoneventrici.feedly.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simoneventrici.feedly.presentation.weather.components.CurrentWeatherBox
import com.simoneventrici.feedly.presentation.weather.components.DailyWeatherBox
import com.simoneventrici.feedly.ui.theme.LighterBlack

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    navController: NavController
) {
    val weatherState = weatherViewModel.currentWeatherStatus.value
    val geoLocalizationStatus = weatherViewModel.geoLocalizationStatus.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
    ) {
        weatherState.data?.let {
            CurrentWeatherBox(weatherOverview = it, geoLocalization = geoLocalizationStatus, navController = navController)
            Spacer(modifier = Modifier.height(20.dp))
            DailyWeatherBox(weatherOverview = it)
        }
    }
}