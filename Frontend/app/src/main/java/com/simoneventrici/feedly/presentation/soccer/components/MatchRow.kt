package com.simoneventrici.feedly.presentation.soccer.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simoneventrici.feedly.model.TeamMatch
import com.simoneventrici.feedly.ui.theme.WhiteColor
import com.simoneventrici.feedly.ui.theme.WhiteDark2
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
@Composable
fun MatchRow(
    match: TeamMatch
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // è la colonna contenente i loghi delle due squadre e il nome della lega dove hanno giocato
        Column(
            modifier = Modifier.widthIn(0.dp, 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                TeamLogoImage(imageUrl = match.getTeamLogoById(match.homeTeamId))
                Spacer(modifier = Modifier.width(5.dp))
                TeamLogoImage(imageUrl = match.getTeamLogoById(match.awayTeamId))
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = match.leagueName,
                color = WhiteDark2,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.weight(1f))

        // nomi delle squadre e data della partita
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${match.homeTeamName} vs ${match.awayTeamName}",
                color = WhiteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = SimpleDateFormat("dd MMM yyyy HH:mm").format(match.timestamp * 1000),
                color = WhiteDark2,
                fontSize = 14.sp,
            )
        }

        Spacer(Modifier.weight(1f))

        // punteggio della partita
        Text(
            text = "${match.homeScore} - ${match.awayScore}",
            color = WhiteColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
