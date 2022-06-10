package com.simoneventrici.feedly.presentation.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.model.WeatherIconParser
import com.simoneventrici.feedly.model.WeatherOverview
import com.simoneventrici.feedly.ui.theme.WhiteColor
import com.simoneventrici.feedly.ui.theme.WhiteDark2
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun DailyWeatherBox(
    weatherOverview: WeatherOverview
) {
    val weatherList = weatherOverview.dailyWeather

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        weatherList.forEach { weatherInfo ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat("EEEE  d", Locale.getDefault()).format(weatherInfo.time * 1000).replaceFirstChar { it.uppercase() },
                    color = WhiteColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(190.dp)
                )
                
                //Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = WeatherIconParser.parse(weatherInfo.weatherIconCode).res_id),
                    contentDescription = "weather icon",
                    modifier = Modifier.size(38.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = CenterHorizontally) {
                    Row(
                        verticalAlignment = Bottom
                    ) {
                        Text(
                            text = weatherInfo.maxTemp?.roundToInt()?.toString()?.plus("°") ?: "--",
                            color = WhiteColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = weatherInfo.minTemp?.roundToInt()?.toString()?.plus("°") ?: "--",
                            color = WhiteDark2,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = LocalContext.current.getString(R.string.wind) + "  ${String.format("%.1f", weatherInfo.windSpeed)} km/h",
                        color = WhiteDark2,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}