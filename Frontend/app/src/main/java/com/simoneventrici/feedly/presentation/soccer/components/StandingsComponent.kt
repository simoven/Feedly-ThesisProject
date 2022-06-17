package com.simoneventrici.feedly.presentation.soccer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.model.LeagueStandings
import com.simoneventrici.feedly.presentation.soccer.SoccerViewModel
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor


@Composable
fun StandingsRow(
    standingsItem: LeagueStandings
) {
    val textStyle = TextStyle(
        color = WhiteColor,
        fontSize = 16.sp,
    )

    val items = listOf(
        standingsItem.points,
        standingsItem.played,
        standingsItem.matchesWon,
        standingsItem.matchesDraw,
        standingsItem.matchesLost,
        standingsItem.goalScored,
        standingsItem.goalAgainst
    )

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = standingsItem.rank.toString(),
            style = textStyle,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold
        )

        Row(Modifier.weight(5f)) {
            Image(
                painter = rememberAsyncImagePainter(model = standingsItem.getTeamLogo()),
                contentDescription = "team logo",
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = standingsItem.teamName,
                style = textStyle,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        items.forEachIndexed { idx, it ->
            var modifier: Modifier = Modifier
            if(idx != items.size - 1)
                modifier = Modifier.weight(1f)

            // metto il semibold solo ai punti, ovvero il primo elemento della lista
            Text(
                text = it.toString(),
                style = textStyle,
                modifier = modifier,
                fontWeight = if(idx == 0) FontWeight.SemiBold else FontWeight.Medium,
            )
        }
    }
}

@Composable
fun StandingsBox(
    standings: List<LeagueStandings>
) {
    val legendTextStyle = TextStyle(
        color = WhiteColor,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold
    )

    val legends = listOf("Pts", "P", "W", "N", "L", "GF", "GA")

    // è la riga con la descrizione della tabella, ovvero punti, giornate, vinte, perse, ecc..
    Row(Modifier.fillMaxWidth()) {
        Text(text = "#", style = legendTextStyle, modifier = Modifier.weight(1f))

        Text(
            text = "Team",
            style = legendTextStyle,
            modifier = Modifier.weight(5f)
        )

        legends.forEachIndexed { idx, it ->
            var modifier: Modifier = Modifier
            if(idx != legends.size - 1)
                modifier = Modifier.weight(1f)

            Text(text = it, style = legendTextStyle, modifier = modifier)
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        standings.forEach {
            StandingsRow(standingsItem = it)
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun StandingsComponent(
    soccerViewModel: SoccerViewModel,
    standings: List<LeagueStandings>
) {

    var dividerYOffset by remember { mutableStateOf(0f) }
    val scrollableModifier = Modifier.pointerInput(Unit) {
        detectDragGestures { _, dragAmount ->
            dividerYOffset += dragAmount.y

            if(dividerYOffset >= 20f) {
                soccerViewModel.dividerScrolled(false)
                dividerYOffset = 0f
            }
            if(dividerYOffset <= -20f) {
                soccerViewModel.dividerScrolled(true)
                dividerYOffset = 0f
            }
        }
    }


    Column(scrollableModifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
            Text(
                text = LocalContext.current.getString(R.string.standings),
                color = WhiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = LocalContext.current.getString(R.string.change_league),
                color = MainGreen,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color(0xFF202020))
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(scrollableModifier.height(15.dp))
                Divider(
                    scrollableModifier
                        .height(4.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(WhiteColor)
                )
                Spacer(scrollableModifier.height(15.dp))

                StandingsBox(standings = standings)
            }
        }
    }
}