package com.playground.navigationapp.navigation

sealed class AppScreens(val route: String) {
    object HomeScreen: AppScreens("home")
    object DetailScreen: AppScreens("detail")
}