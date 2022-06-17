package com.simoneventrici.feedly.presentation.soccer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.soccer.components.StandingsComponent
import com.simoneventrici.feedly.presentation.soccer.components.FavouriteTeamMatchesSection
import com.simoneventrici.feedly.presentation.soccer.components.TopBar
import com.simoneventrici.feedly.ui.theme.LighterBlack

@Composable
fun SoccerScreen(
    soccerViewModel: SoccerViewModel = hiltViewModel(),
    navController: NavController
) {
    val favTeamMatches = soccerViewModel.userFavouriteTeamsMatches.value
    val currentStandings = soccerViewModel.currentStandings.value

    Column(
        modifier = Modifier
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp)
    ) {
        TopBar(navController = navController)

        Spacer(modifier = Modifier.height(15.dp))
        FavouriteTeamMatchesSection(soccerViewModel = soccerViewModel, teamMatches = favTeamMatches)

        Spacer(modifier = Modifier.height(15.dp))
        StandingsComponent(soccerViewModel = soccerViewModel, standings = currentStandings)
    }
}