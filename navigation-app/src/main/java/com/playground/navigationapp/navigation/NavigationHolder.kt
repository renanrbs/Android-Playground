package com.playground.navigationapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.playground.navigationapp.screens.DetailScreen
import com.playground.navigationapp.screens.HomeScreen

@Composable
fun NavigationHolder() {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = AppScreens.HomeScreen.route
    ) {
        composable(route = AppScreens.HomeScreen.route) { HomeScreen(navigationController) }
        composable(
            route = "${AppScreens.DetailScreen.route}?textInput={textInput}",
            arguments = listOf(navArgument("textInput") {
                type = NavType.StringType
                nullable = true
            })
        ) { from ->
            val textInputted = from.arguments?.getString("textInput")
            DetailScreen(navigationController, textInputted)
        }
    }
}