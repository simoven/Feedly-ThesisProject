package com.simoneventrici.feedly.presentation.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.model.News
import com.simoneventrici.feedly.presentation.components.EmojiOverviewBar
import com.simoneventrici.feedly.ui.theme.WhiteDark1
import com.simoneventrici.feedly.ui.theme.WhiteDark2

@Composable
fun NewsCard(
    news: News,
    reactions: Map<String, Int>,
    userReaction: String?,
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = modifier
            .fillMaxWidth(.9f)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(20.dp))) {
            Image(
                painter = rememberAsyncImagePainter(news.imageUrl),
                contentDescription = "News image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { sizeImage = it.size }
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(.7f)),
                        startY = sizeImage.height.toFloat() * 0.65f,
                        endY = sizeImage.height.toFloat()
                    )
                )
            )
        }
        // se c'è almeno una reazione, mostro il box con le emoji
        // se non ce ne sono, mantengo lo spazio per e
        if(reactions.keys.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .width(340.dp)
                    .offset(y = (-20).dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                EmojiOverviewBar(emojis = reactions, selectedEmoji = userReaction)
            }
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .offset(y = (-10).dp)
        ) {
            Text(
                text = news.title,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row() {
                Text(
                    text = news.sourceName,
                    color = WhiteDark1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = news.publishedDate.split("T")[0],
                    color = WhiteDark2,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}