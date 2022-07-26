package com.simoneventrici.feedly.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simoneventrici.feedly.R
import com.simoneventrici.feedly.presentation.authentication.AuthViewModel
import com.simoneventrici.feedly.presentation.authentication.LoginScreen
import com.simoneventrici.feedly.presentation.authentication.SignupScreen
import com.simoneventrici.feedly.presentation.authentication.WelcomeScreen
import com.simoneventrici.feedly.presentation.crypto.CryptoScreen
import com.simoneventrici.feedly.presentation.crypto.CryptoViewModel
import com.simoneventrici.feedly.presentation.crypto.components.FavouriteCryptoChooser
import com.simoneventrici.feedly.presentation.explore.ExploreScreen
import com.simoneventrici.feedly.presentation.explore.ExploreViewModel
import com.simoneventrici.feedly.presentation.explore.components.SearchNewsPage
import com.simoneventrici.feedly.presentation.home.HomeScreen
import com.simoneventrici.feedly.presentation.profile.ProfileScreen
import com.simoneventrici.feedly.presentation.soccer.SoccerScreen
import com.simoneventrici.feedly.presentation.soccer.SoccerViewModel
import com.simoneventrici.feedly.presentation.soccer.components.ManageLeagueScreen
import com.simoneventrici.feedly.presentation.soccer.components.ManageTeamsScreen
import com.simoneventrici.feedly.presentation.stocks.StockScreen
import com.simoneventrici.feedly.presentation.stocks.StocksViewModel
import com.simoneventrici.feedly.presentation.stocks.components.FavouriteStockChooser
import com.simoneventrici.feedly.presentation.weather.WeatherScreen
import com.simoneventrici.feedly.presentation.weather.WeatherViewModel
import com.simoneventrici.feedly.presentation.weather.components.CityChooserScreen
import com.simoneventrici.feedly.ui.theme.LighterGrayNavBar

data class NavigationItem(
    val uncheckedIcon: Int,
    val checkedIcon: Int,
    val route: String,
    val title: String
)

@Composable
fun AppRouter() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val userState = authViewModel.userState.value
    val isAuthenticating = authViewModel.isAuthenticating.value

    val authenticated = userState != null

    if(!isAuthenticating) {
        if (!authenticated) {
            AuthenticationNavigator(authViewModel)
        } else {
            MainScreen(authViewModel)
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel) {
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
            Navigator(controller = navController, authViewModel = authViewModel)
        }
    }
}

@Composable
fun Navigator(
    controller: NavHostController,
    authViewModel: AuthViewModel
) {
    val exploreViewModel: ExploreViewModel = hiltViewModel()
    val cryptoViewModel: CryptoViewModel = hiltViewModel()
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    // se cambiano i preferiti, allora riapplico il filtro per mostrare le altre crypto nella schermata di aggiunta
    val userFavouritesCrypto = remember(cryptoViewModel.favouritesCrypto.value.data?.size ) { cryptoViewModel.favouritesCrypto.value.data?.map { it.ticker } ?: emptyList() }
    val soccerViewModel: SoccerViewModel = hiltViewModel()
    val stocksViewModel: StocksViewModel = hiltViewModel()
    val userFavouriteStocks = remember(stocksViewModel.favouriteStocks.value.data?.size ) { stocksViewModel.favouriteStocks.value.data?.map { it.ticker } ?: emptyList() }

    NavHost(navController = controller, startDestination = Screen.ExploreScreen.route) {
        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(exploreViewModel, navController = controller)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                navController = controller,
                currentWeatherInfo = weatherViewModel.currentWeatherStatus.value.data?.currentWeather,
                user = authViewModel.userState.value
            )
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(authViewModel = authViewModel)
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
        composable(route = Screen.WeatherScreen.route) {
            WeatherScreen(weatherViewModel = weatherViewModel, navController = controller)
        }
        composable(route = Screen.CityChooserScreen.route) {
            CityChooserScreen(navController = controller, weatherViewModel = weatherViewModel)
        }
        composable(route = Screen.NewsSearchScreen.route) {
            SearchNewsPage(exploreViewModel = exploreViewModel, navController = controller)
        }
        composable(route = Screen.SoccerScreen.route) {
            SoccerScreen(soccerViewModel = soccerViewModel, navController = controller)
        }
        composable(route = Screen.ManageSoccerTeamsScreen.route) {
            ManageTeamsScreen(soccerViewModel = soccerViewModel, navController = controller)
        }
        composable(route = Screen.StockScreen.route) {
            StockScreen(navController = controller, stocksViewModel = stocksViewModel)
        }
        composable(route = Screen.AddNewStockScreen.route) {
            FavouriteStockChooser(
                stocksViewModel = stocksViewModel,
                allStocks = stocksViewModel.allStocks.value.filter { !userFavouriteStocks.contains(it.ticker) },
                navController = controller
            )
        }
        composable(route = Screen.ChooseFavouriteLeagueScreen.route) {
            ManageLeagueScreen(soccerViewModel = soccerViewModel, navController = controller)
        }
    }
}

@Composable
fun AuthenticationNavigator(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
        composable(route = Screen.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = Screen.SignUpScreen.route) {
            SignupScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}