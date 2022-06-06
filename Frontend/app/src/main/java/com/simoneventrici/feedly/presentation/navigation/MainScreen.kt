package com.simoneventrici.feedly.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.crypto.CryptoScreen
import com.simoneventrici.feedly.presentation.crypto.CryptoViewModel
import com.simoneventrici.feedly.presentation.crypto.components.FavouriteCryptoChooser
import com.simoneventrici.feedly.presentation.explore.ExploreScreen
import com.simoneventrici.feedly.presentation.home.HomeScreen
import com.simoneventrici.feedly.presentation.profile.ProfileScreen
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.LighterGrayNavBar

data class NavigationItem(
    val uncheckedIcon: Int,
    val checkedIcon: Int,
    val route: String,
    val title: String
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavBar(
                items = listOf(
                    NavigationItem(R.drawable.news_unchecked, R.drawable.news_checked, Screen.ExploreScreen.route, stringResource(R.string.explore_btn_content)),
                    NavigationItem(R.drawable.home_unchecked, R.drawable.home_checked, Screen.HomeScreen.route, stringResource(R.string.for_you_btn_content)),
                    NavigationItem(R.drawable.profile_unchecked, R.drawable.profile_checked, Screen.ProfileScreen.route, stringResource(R.string.profile_btn_content)),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                })
        },
        backgroundColor = LighterGrayNavBar
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Dentro lo scaffold c'è il navHost che cambia le schermate
            Navigator(controller = navController)
        }
    }
}

@Composable
fun Navigator(
    controller: NavHostController
) {
    val cryptoViewModel: CryptoViewModel = hiltViewModel()
    val userFavouritesCrypto = cryptoViewModel.favouritesCrypto.value.data?.map { it.ticker } ?: emptyList()

    NavHost(navController = controller, startDestination = Screen.ExploreScreen.route) {
        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen()
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(controller)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen()
        }
        composable(route = Screen.CryptoScreen.route) {
            CryptoScreen(navController = controller, cryptoViewModel = cryptoViewModel)
        }
        composable(route = Screen.AddNewCryptoScreen.route) {
            FavouriteCryptoChooser(
                // prendo le cripto che non sono già state preferite dall'utente
                cryptoViewModel,
                allCryptos = cryptoViewModel.allCryptos.value.filter { !userFavouritesCrypto.contains(it.ticker) },
                navController = controller)
        }
    }
}