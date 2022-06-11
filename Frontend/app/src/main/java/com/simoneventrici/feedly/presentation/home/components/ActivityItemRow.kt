package com.simoneventrici.feedly.presentation.home.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.model.Emoji
import com.simoneventrici.feedly.model.RecentActivity
import com.simoneventrici.feedly.ui.theme.WhiteDark1
import com.simoneventrici.feedly.ui.theme.WhiteDark2

// in base al tipo di attività recente, ottengo la stringa corrispondente
fun activityTypeParse(activityType: RecentActivity.ActivityType, isPart1: Boolean, context: Context): String {
    return when(activityType) {
        is RecentActivity.ActivityType.NewsReaction -> {
            if(activityType.isAdding) {
                if(isPart1) context.getString(R.string.adding_reaction_part1) else context.getString(R.string.adding_reaction_part2)
            }
            else {
                if(isPart1) context.getString(R.string.remove_reaction_part1) else context.getString(R.string.remove_reaction_part2)
            }
        }
        is RecentActivity.ActivityType.CryptoFavourite -> {
            if(activityType.isAdding) {
                if(isPart1) context.getString(R.string.adding_ticker_part1) else context.getString(R.string.adding_cripto_part2)
            }
            else {
                if(isPart1) context.getString(R.string.remove_ticker_part1) else context.getString(R.string.remove_cripto_part2)
            }
        }
        is RecentActivity.ActivityType.StockFavourite -> {
            if(activityType.isAdding) {
                if(isPart1) context.getString(R.string.adding_ticker_part1) else context.getString(R.string.adding_asset_part2)
            }
            else {
                if(isPart1) context.getString(R.string.remove_ticker_part1) else context.getString(R.string.remove_asset_part2)
            }
        }
        is RecentActivity.ActivityType.TeamFavourite -> {
            if(activityType.isAdding) {
                if(isPart1) context.getString(R.string.adding_ticker_part1) else context.getString(R.string.adding_team_part2)
            }
            else {
                if(isPart1) context.getString(R.string.remove_ticker_part1) else context.getString(R.string.remove_team_part2)
            }
        }
    }
}

@Composable
fun ActivityItemRow(
    activity: RecentActivity
) {
    val textStyle = TextStyle(
        color = WhiteDark1,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activityTypeParse(
                        activity.activityType,
                        isPart1 = true,
                        LocalContext.current
                    ),
                    color = textStyle.color,
                    fontSize = textStyle.fontSize,
                    fontWeight = textStyle.fontWeight
                )

                // se l'attività recente è di tipo news reaction, allora mi serve l'immagine per l'emoji
                if (activity.activityType is RecentActivity.ActivityType.NewsReaction) {
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(
                            Emoji.Loader.load(activity.activityParam2 ?: "").res_id
                        ),
                        modifier = Modifier.size(32.dp),
                        contentDescription = "Emoji of reaction"
                    )
                    Spacer(Modifier.width(4.dp))
                } else { // altrimenti basta del testo semplice
                    Text(
                        text = activity.activityParam1,
                        color = WhiteDark2,
                        fontSize = textStyle.fontSize,
                        fontWeight = textStyle.fontWeight
                    )
                }

                Text(
                    text = activityTypeParse(
                        activity.activityType,
                        isPart1 = false,
                        LocalContext.current
                    ),
                    color = textStyle.color,
                    fontSize = textStyle.fontSize,
                    fontWeight = textStyle.fontWeight
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = activity.date,
                color = WhiteDark2,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500
            )
        }

        Spacer(Modifier.weight(1f))

        Image(
            painter = rememberAsyncImagePainter(activity.activityParam3),
            contentDescription = "Activity Image",
            modifier = Modifier
                .width(64.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )
    }
}