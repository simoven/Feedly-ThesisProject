package com.simoneventrici.feedly.presentation.soccer.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.DataState
import com.simoneventrici.feedly.model.TeamMatch
import com.simoneventrici.feedly.presentation.components.ShimmerEffectLoader
import com.simoneventrici.feedly.presentation.navigation.Screen
import com.simoneventrici.feedly.presentation.soccer.SoccerViewModel
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun TeamLogoImage(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl),
        modifier = Modifier.size(40.dp),
        contentScale = ContentScale.Fit,
        contentDescription = "Team logo"
    )
}

@Composable
fun FavouriteTeamMatchesSection(
    soccerViewModel: SoccerViewModel,
    teamMatches: DataState<List<TeamMatch>>,
    navController: NavController
) {
    val scrollUpState = soccerViewModel.scrollUp.observeAsState()
    val heightInDp = with(LocalDensity.current) {
        soccerViewModel.matchesBoxHeight.value.toDp()
    }
    val height by animateDpAsState(targetValue = if(scrollUpState.value == true) 0.dp else heightInDp)

    // questo modifier si riduce per far scrollare il componennte che sta in basso, ovvero la classifica
    val modifier = if(soccerViewModel.matchesBoxHeight.value == 0) {
        Modifier.onGloballyPositioned { soccerViewModel.matchesBoxHeight.value = it.size.height }
    } else
        Modifier.height(height)

    Box(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(.75f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = LocalContext.current.getString(R.string.latest_matches),
                    color = WhiteColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = LocalContext.current.getString(R.string.manage_teams),
                    color = MainGreen,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { navController.navigate(Screen.ManageSoccerTeamsScreen.route) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                teamMatches.data?.let {
                    it.forEach { match ->
                        MatchRow(match = match)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                
                if(teamMatches is DataState.Loading) {
                    repeat(6) {
                        ShimmerEffectLoader {
                            MatchRowLoader(brush = it)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // è un piccolo box che applica un linear gradient alla fine della lista di partite
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            LighterBlack
                        ),
                        startY = 0f,
                        endY = 100f
                    ),
                ),
        )
    }
}