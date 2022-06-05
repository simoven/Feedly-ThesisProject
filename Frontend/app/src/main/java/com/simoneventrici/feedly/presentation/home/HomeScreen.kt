package com.simoneventrici.feedly.presentation.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.explore.components.ScrollableTopBar
import com.simoneventrici.feedly.presentation.home.components.ActivityItemRow
import com.simoneventrici.feedly.presentation.home.components.TopicChooser
import com.simoneventrici.feedly.presentation.navigation.PageSwiper
import com.simoneventrici.feedly.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

fun getWelcomeText(context: Context): String {
    val currentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val hour = (currentTime.split(":")[0]).toInt()
    val minutes = (currentTime.split(":")[0]).toInt()
    return when(hour * 60 + minutes) {
        // 5:00-14:00
        in 300..840 -> context.getString(R.string.good_morning)
        // 14:01-19:00
        in 841..1140 -> context.getString(R.string.good_afternoon)
        else -> context.getString(R.string.good_evening)
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val recentActivity = homeViewModel.recentActivity.value

    val gradient = Brush.radialGradient(
        colors = listOf(
            MainPurple,
            LighterBlack,
        ),
        center = Offset(x = -150f, y = -600f),
        radius = 1600f
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(gradient))

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(
                        text = getWelcomeText(LocalContext.current),
                        color = WhiteColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "username",
                        color = WhiteDark1,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.weather_02d),
                        contentDescription = "Current weather icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "28 °C",
                        color = WhiteDark1,
                        fontSize = 18.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = LocalContext.current.getString(R.string.your_interests),
                color = WhiteColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(20.dp))
            TopicChooser()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = LocalContext.current.getString(R.string.your_activity),
                color = WhiteColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                recentActivity.data?.forEach {
                    ActivityItemRow(activity = it)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }

    }
}