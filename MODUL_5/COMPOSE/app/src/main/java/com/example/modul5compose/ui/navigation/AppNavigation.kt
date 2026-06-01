package com.example.modul5compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modul5compose.ui.screen.DetailScreen
import com.example.modul5compose.ui.screen.HomeScreen
import com.example.modul5compose.ui.screen.SettingsScreen
import com.example.modul5compose.viewmodel.AnimeViewModel
import com.example.modul5compose.viewmodel.AnimeViewModelFactory

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val vm: AnimeViewModel = viewModel(factory = AnimeViewModelFactory(LocalContext.current))

    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav, vm) }
        composable("detail/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull()
            DetailScreen(nav, vm, id)
        }
        composable("settings") { SettingsScreen(nav) }
    }
}