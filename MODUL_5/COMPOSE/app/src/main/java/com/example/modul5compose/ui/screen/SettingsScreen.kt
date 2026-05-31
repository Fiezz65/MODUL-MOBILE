package com.example.modul5compose.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.modul5compose.R
import com.example.modul5compose.data.datastore.AppPreferencesRepository

@Composable
fun SettingsScreen(navController: androidx.navigation.NavController) {
    val activity = LocalActivity.current
    val blue = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))

    Scaffold(
        topBar = {
            Surface(color = Color.Black, shadowElevation = 4.dp) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                    Text(
                        stringResource(R.string.title_settings),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LanguageButton(stringResource(R.string.btn_indonesia), blue) {
                activity?.let {
                    changeLanguage(it, "id-ID")
                    it.recreate()
                }
            }
            Spacer(Modifier.height(16.dp))
            LanguageButton(stringResource(R.string.btn_english), blue) {
                activity?.let {
                    changeLanguage(it, "en-US")
                    it.recreate()
                }
            }
        }
    }
}

@Composable
fun LanguageButton(text: String, colors: ButtonColors, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        colors = colors
    ) {
        Text(text)
    }
}

private fun changeLanguage(context: android.content.Context, language: String) {
    AppPreferencesRepository.saveLanguage(context, language)
    AppPreferencesRepository.applySavedLanguage(context)
}