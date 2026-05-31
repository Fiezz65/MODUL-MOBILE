package com.example.modul5compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modul5compose.ui.screen.DetailScreen
import com.example.modul5compose.ui.screen.HomeScreen
import com.example.modul5compose.ui.screen.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("detail/{animeId}") { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toIntOrNull()
            DetailScreen(navController = navController, animeId = animeId)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}