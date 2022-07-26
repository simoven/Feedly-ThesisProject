package com.simoneventrici.feedly.presentation.soccer.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.soccer.SoccerViewModel
import com.simoneventrici.feedly.ui.theme.LighterBlack
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.WhiteColor

fun getLeagueLogoUrl(leagueId: Int): String {
    return "https://media.api-sports.io/football/leagues/$leagueId.png"
}

@Composable
fun ManageLeagueScreen(
    soccerViewModel: SoccerViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .background(LighterBlack)
            .padding(top = getSystemStatusbarHeightInDp(LocalContext.current).dp)
    ) {
        TopBar(navController = navController, textId = R.string.choose_league)
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            soccerViewModel.allLeagues.value.forEachIndexed { idx, league ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { soccerViewModel.saveFavouriteLeague(league.leagueId); navController.popBackStack() }
                        .padding(horizontal = 15.dp, vertical = 12.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(getLeagueLogoUrl(league.leagueId)),
                        modifier = Modifier.size(42.dp),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = league.name,
                        color = WhiteColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if(idx != soccerViewModel.allLeagues.value.size - 1) {
                    Divider(Modifier.fillMaxWidth(.95f).height(1.dp).background(LighterGray))
                }
            }
        }
    }
}