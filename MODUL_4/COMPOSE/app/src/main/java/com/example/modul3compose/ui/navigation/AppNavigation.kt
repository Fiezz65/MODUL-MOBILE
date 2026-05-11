package com.example.modul3compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modul3compose.ui.screen.DetailScreen
import com.example.modul3compose.ui.screen.HomeScreen
import com.example.modul3compose.ui.screen.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("detail/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
            DetailScreen(navController = navController, gameId = gameId)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}