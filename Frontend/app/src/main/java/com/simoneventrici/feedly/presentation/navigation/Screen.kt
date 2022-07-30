package com.simoneventrici.feedly.presentation.navigation

sealed class Screen(val route: String) {
    object ExploreScreen: Screen("explore")
    object HomeScreen: Screen("home")
    object ProfileScreen: Screen("profile")
    object CryptoScreen: Screen("crypto")
    object AddNewCryptoScreen: Screen("add_new_crypto")
    object WeatherScreen: Screen("weather")
    object CityChooserScreen: Screen("city_chooser")
    object NewsSearchScreen: Screen("news_search")
    object SoccerScreen: Screen("soccer")
    object ManageSoccerTeamsScreen: Screen("manage_soccer_teams")
    object StockScreen: Screen("stocks")
    object AddNewStockScreen: Screen("add_new_stock")
    object ChooseFavouriteLeagueScreen: Screen("fav_league_screen")
    object WelcomeScreen: Screen("welcome")
    object LoginScreen: Screen("login")
    object SignUpScreen: Screen("sign_up")
    object PasswordChangeScreen: Screen("password_change")
}