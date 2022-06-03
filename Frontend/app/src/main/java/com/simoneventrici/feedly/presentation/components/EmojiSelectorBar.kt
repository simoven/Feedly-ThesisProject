package com.simoneventrici.feedly.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.model.Emoji
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.MainGreen
import kotlinx.coroutines.launch

@Composable
fun EmojiSelectorBar(
    emojis: List<Emoji> = listOf(
        Emoji.ThumbsUp(),
        Emoji.WowFace(),
        Emoji.LaughFace(),
        Emoji.AngryFace(),
        Emoji.SadFace(),
        Emoji.Heart()
    ),
    onEmojiClicked: (Emoji) -> Unit
) {
    var sizeState by remember { mutableStateOf(0.dp) }
    val sizeAnim by animateDpAsState(targetValue = sizeState)
    val launched = true

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(LighterGray)
            .padding(vertical = 6.dp)
            .width(sizeAnim)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(6.dp))
        emojis.forEach { emoji ->
            // Il box che contiene le varie emoji
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Transparent)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Image(
                    painter = painterResource(id = emoji.res_id),
                    contentDescription = "Emoji",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onEmojiClicked(emoji)
                        }
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
    }

    // viene lanciato solo una volta alla composizione e serve a fare l'animazione di apertura
    LaunchedEffect(key1 = launched) {
        sizeState += 282.dp
    }
}