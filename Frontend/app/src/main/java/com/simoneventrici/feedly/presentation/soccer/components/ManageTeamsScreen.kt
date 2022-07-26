package com.simoneventrici.feedly.presentation.soccer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.commons.getSystemStatusbarHeightInDp
import com.simoneventrici.feedly.presentation.soccer.SoccerViewModel
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun ManageTeamsScreen(
    soccerViewModel: SoccerViewModel,
    navController: NavController,
) {
    val teamChoosen = remember { soccerViewModel.userFavouriteTeams.value.map { it.teamId }.toMutableList() }
    val allTeams = remember(soccerViewModel.allTeams.value.size) { soccerViewModel.allTeams.value.sortedBy { it.name }}

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = BottomEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = getSystemStatusbarHeightInDp(LocalContext.current).dp,
                    start = 20.dp,
                    end = 20.dp
                )
        ) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 10.dp),
                verticalAlignment = CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow_icon),
                    contentDescription = "back button",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = LocalContext.current.getString(R.string.manage_favourite_teams),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WhiteColor
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                allTeams.forEachIndexed { idx, team ->
                    val checked = remember { mutableStateOf(team.teamId in teamChoosen) }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = team.getTeamLogoById()),
                            contentDescription = "Team logo",
                            modifier = Modifier.size(42.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = team.name,
                            color = WhiteColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(
                            checked = checked.value,
                            onCheckedChange = {
                                checked.value = !checked.value
                                if(checked.value)
                                    teamChoosen.add(team.teamId)
                                else
                                    teamChoosen.remove(team.teamId)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MainGreen
                            )
                        )
                    }
                    
                    if(idx == soccerViewModel.allTeams.value.size - 1) {
                        Spacer(modifier = Modifier.height(63.dp))
                    }
                }
            }
        }

        Box(
            modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
        ) {
            FloatingActionButton(
                backgroundColor = MainGreen,
                onClick = {
                    soccerViewModel.setUserFavouritesTeams(teamChoosen)
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check_icon),
                    contentDescription = "Confirm button",
                    tint = WhiteColor,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}