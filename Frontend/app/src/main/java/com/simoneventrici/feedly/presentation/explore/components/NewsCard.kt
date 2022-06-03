package com.simoneventrici.feedly.presentation.explore.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.model.Emoji
import com.simoneventrici.feedly.model.News
import com.simoneventrici.feedly.presentation.components.EmojiOverviewBar
import com.simoneventrici.feedly.presentation.components.EmojiSelectorBar
import com.simoneventrici.feedly.ui.theme.WhiteColor
import com.simoneventrici.feedly.ui.theme.WhiteDark1
import com.simoneventrici.feedly.ui.theme.WhiteDark2

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsCard(
    news: News,
    reactions: Map<String, Int>,
    userReaction: String?,
    onLongClick: () -> Unit,
    onEmojiClicked: (Emoji) -> Unit,
    emojiBoxActive: Boolean,
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(news.newsUrl)) }

    Column(
        modifier = modifier.fillMaxWidth(.9f)
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
                    .combinedClickable(
                        onClick = { context.startActivity(intent) },
                        onLongClick = { onLongClick() }
                    ),
            )
            // Il linear gradient che si sovrappone all'immagine
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
        // se non ce ne sono, mantengo lo spazio per evitare  sproporzioni
        if(reactions.keys.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
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
            // è la barra dove seleziono le emoji per una reazione
            if(emojiBoxActive) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .offset(y = (-5).dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    EmojiSelectorBar(onEmojiClicked = onEmojiClicked)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            
            Text(
                text = news.title,
                fontWeight = FontWeight.SemiBold,
                color = WhiteColor,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
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