package com.simoneventrici.feedly.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.ui.theme.WhiteColor

data class ContentHolder(
    val image: Int,
    val title: String,
    val callback: () -> Unit
)

@Composable
fun ContentCard(
    content: ContentHolder,
    textAlignLeft: Boolean
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = Modifier
        .width(170.dp)
        .height(120.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable { content.callback() }
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { sizeImage = it.size },
            painter = painterResource(id = content.image),
            contentDescription = content.title,
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(.85f)),
                    startY = sizeImage.height.toFloat() * 0.45f,
                    endY = sizeImage.height.toFloat()
                )
            )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = if(textAlignLeft) Alignment.BottomStart else Alignment.BottomEnd
        ) {
            Text(
                text = content.title,
                color = WhiteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.offset(x = if(textAlignLeft) 14.dp else (-14).dp, y = (-8).dp)
            )
        }
    }
}

@Composable
fun TopicChooser(
    navController: NavController,
    content: List<ContentHolder> = listOf(
        ContentHolder(R.drawable.weather_background, LocalContext.current.getString(R.string.weather)) { navController.navigate(Screen.WeatherScreen.route)},
        ContentHolder(R.drawable.soccer_background, LocalContext.current.getString(R.string.soccer)) { navController.navigate(Screen.SoccerScreen.route)},
        ContentHolder(R.drawable.finance_background, LocalContext.current.getString(R.string.finance)) { navController.navigate(Screen.StockScreen.route)},
        ContentHolder(R.drawable.crypto_background, LocalContext.current.getString(R.string.crypto)) { navController.navigate(Screen.CryptoScreen.route) }
    )
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ContentCard(content = content[0], true)
            ContentCard(content = content[1], false)
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ContentCard(content = content[2], true)
            ContentCard(content = content[3], false)
        }

    }
}