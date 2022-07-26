package com.simoneventrici.feedly.presentation.weather.components

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.model.GeoLocalizationInfo
import com.simoneventrici.feedly.model.WeatherIconParser
import com.simoneventrici.feedly.model.WeatherOverview
import com.simoneventrici.feedly.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt


@SuppressLint("SimpleDateFormat")
@Composable
fun HourlyWeatherBox(
    weatherOverview: WeatherOverview
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(WeatherBoxBackground)
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            weatherOverview.hourlyWeather.subList(0,10).forEach { weatherInfo ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = SimpleDateFormat("HH:mm").format(Date(weatherInfo.time * 1000)),
                        color = WhiteDark1,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Image(
                        painter = painterResource(id = WeatherIconParser.parse(weatherInfo.weatherIconCode).res_id),
                        modifier = Modifier.size(32.dp),
                        contentDescription = "weather icon"
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${weatherInfo.temperature.roundToInt()}°",
                        color = WhiteColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }
                Spacer(modifier = Modifier.width(35.dp))
            }
        }
    }
}


@Composable
fun CurrentWeatherBox(
    weatherOverview: WeatherOverview,
    geoLocalization: GeoLocalizationInfo?,
    navController: NavController,
    onSearchClicked: () -> Unit
) {
    val currentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val hour = (currentTime.split(":")[0]).toInt()
    val minutes = (currentTime.split(":")[0]).toInt()

    // scelgo i colori di background in base a che ora è
    val background = Brush.verticalGradient(
        // tra le 5 e le 18 scelgo un background chiaro
        colors = if(hour * 60 + minutes in 300..1080) listOf(
            WeatherDayTop,
            WeatherDayBottom,
        ) else listOf( // altrimenti background scuro
            WeatherNightTop,
            WeatherNightBottom
        ),
        startY = 0f,
        endY = 1000f
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(background)
            .padding(
                top = getSystemStatusbarHeightInDp(LocalContext.current).dp + 10.dp,
                bottom = 20.dp,
                start = 20.dp,
                end = 20.dp
            )

    ) {
        // questa riga è la top bar con back button, search button e stringa del luogo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow_icon),
                contentDescription = "back button",
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = geoLocalization?.fullLabel ?: "",
                color = WhiteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                painter = painterResource(id = R.drawable.search_unchecked),
                contentDescription = "search button",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onSearchClicked() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // riga del meteo corrente
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = WeatherIconParser.parse(weatherOverview.currentWeather.weatherIconCode).res_id),
                modifier = Modifier.size(64.dp),
                contentDescription = "weather icon"
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "${round(weatherOverview.currentWeather.temperature).toInt()}° C",
                color = WhiteColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${weatherOverview.currentWeather.weatherDescription.replaceFirstChar { it.uppercase() }} - " +
                        "${LocalContext.current.getString(R.string.feels_like)} ${round(weatherOverview.currentWeather.feelsLike).toInt()}° C",
                color = WhiteDark1,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // la riga con temp min e max
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = LocalContext.current.getString(R.string.today),
                color = WhiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            val minStyle = TextStyle(
                color = WhiteColor,
                fontSize = 16.sp,
            )
            val maxStyle = TextStyle(
                color = WhiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "min",
                style = minStyle
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = weatherOverview.dailyWeather.getOrNull(0)?.minTemp?.roundToInt()?.toString()?.plus("°") ?: "--",
                style = maxStyle
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "max",
                style = minStyle
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = weatherOverview.dailyWeather.getOrNull(0)?.maxTemp?.roundToInt()?.toString()?.plus("°") ?: "--",
                style = maxStyle
            )
        }

        Spacer(Modifier.height(10.dp))

        HourlyWeatherBox(weatherOverview = weatherOverview)
    }
}