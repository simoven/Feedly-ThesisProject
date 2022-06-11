package com.simoneventrici.feedly.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.WeatherIconParser
import com.simoneventrici.feedly.model.WeatherOverview
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.presentation.weather.components.CurrentWeatherBox
import com.simoneventrici.feedly.presentation.weather.components.DailyWeatherBox
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor
import com.simoneventrici.feedly.ui.theme.WhiteDark1

@Composable
fun WeatherErrorContent(
    weatherState: DataState<WeatherOverview>
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = WeatherIconParser.parse("00").res_id),
                contentDescription = "Error icon logo",
                modifier = Modifier.size(82.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if(weatherState is DataState.Error) weatherState.errorMsg ?: LocalContext.current.getString(R.string.unexpected_error_msg)
                       else LocalContext.current.getString(R.string.unexpected_error_msg),
                color = WhiteColor,
                fontSize = 20.sp

            )
        }
    }
}

@Composable
fun SetupFirstLocalization(
    navController: NavController
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = WeatherIconParser.parse("00").res_id),
                contentDescription = "Error icon logo",
                modifier = Modifier.size(82.dp)
            )
            Text(
                text = LocalContext.current.getString(R.string.no_place_setup),
                color = WhiteDark1,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = LocalContext.current.getString(R.string.set_up),
                color = MainGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier.clickable { navController.navigate(Screen.CityChooserScreen.route) }
            )
        }
    }
}

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    navController: NavController
) {
    val weatherState = weatherViewModel.currentWeatherStatus.value
    val geoLocalizationStatus = weatherViewModel.geoLocalizationStatus.value
    val callback = { navController.navigate(Screen.CityChooserScreen.route) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterBlack)
    ) {
        weatherState.data?.let {
            CurrentWeatherBox(
                weatherOverview = it,
                geoLocalization = geoLocalizationStatus,
                navController = navController,
                onSearchClicked = callback
            )
            Spacer(modifier = Modifier.height(20.dp))
            DailyWeatherBox(weatherOverview = it)
        } ?: if(geoLocalizationStatus == null) SetupFirstLocalization(navController = navController) else WeatherErrorContent(weatherState = weatherState)
    }
}