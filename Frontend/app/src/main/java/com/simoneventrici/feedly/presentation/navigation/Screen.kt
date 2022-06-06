package com.simoneventrici.feedly.presentation.navigation

sealed class Screen(val route: String) {
    object ExploreScreen: Screen("explore")
    object HomeScreen: Screen("home")
    object ProfileScreen: Screen("profile")
    object CryptoScreen: Screen("crypto")
    object AddNewCryptoScreen: Screen("add_new_crypto")
}