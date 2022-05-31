package com.simoneventrici.feedly.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
fun EmojiOverviewBar(
    emojis: Map<String, Int>,
    selectedEmoji: String?
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(LighterGray)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(6.dp))
        for((key, value) in emojis) {
            val emoji = Emoji.Loader.load(key)

            // Il box che contiene le varie emoji
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(if (selectedEmoji?.equals(key) == true) MainGreen else Color.Transparent)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = emoji.res_id),
                        contentDescription = "Emoji",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = value.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}