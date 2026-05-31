package com.example.modul5compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modul5compose.viewmodel.AnimeViewModel
import com.example.modul5compose.viewmodel.AnimeViewModelFactory
import com.example.modul5compose.ui.screen.DetailScreen
import com.example.modul5compose.ui.screen.HomeScreen
import com.example.modul5compose.ui.screen.SettingsScreen

@Composable
fun AppNavigation() {
    // 1. Gunakan NavHostController secara eksplisit
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    
    // 2. Ganti nama variabel agar TIDAK bentrok dengan nama fungsi viewModel()
    val animeViewModel: AnimeViewModel = viewModel(
        factory = AnimeViewModelFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController, viewModel = animeViewModel)
        }
        composable("detail/{animeId}") { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toIntOrNull()
            DetailScreen(
                navController = navController,
                animeId = animeId,
                viewModel = animeViewModel
            )
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}