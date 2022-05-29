package com.simoneventrici.feedly.presentation.navigation

sealed class Screen(val route: String) {
    object ExploreScreen: Screen("explore")
    object HomeScreen: Screen("home")
    object ProfileScreen: Screen("profile")
}