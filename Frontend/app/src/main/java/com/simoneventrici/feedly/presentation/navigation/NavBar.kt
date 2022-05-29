package com.simoneventrici.feedly.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.MainGreen

@Composable
fun NavBar(
    items: List<NavigationItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (NavigationItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier,
        backgroundColor = LighterGray,
        elevation = 10.dp
    ) {
        items.forEach() { item ->
            val selected = backStackEntry.value?.destination?.route == item.route
            BottomNavigationItem(
                selected = selected,
                onClick = { if(!selected) onItemClick(item) },
                selectedContentColor = MainGreen,
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = if(selected) item.checkedIcon else item.uncheckedIcon),
                            contentDescription = "Description",
                            //contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = item.title,
                            color = if (selected) MainGreen else Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            )

        }
    }
}