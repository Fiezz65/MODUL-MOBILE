package com.example.modul5compose.ui.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.example.modul5compose.R
import com.example.modul5compose.data.datastore.AppPreferencesRepository

@Composable
fun SettingsScreen(navController: NavController) {
    val activity = LocalActivity.current

    Scaffold(
        topBar = {
            Surface(color = Color.Black, shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                    Text(stringResource(R.string.title_settings), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { p ->
        Column(Modifier.fillMaxSize().padding(p), Arrangement.Center, Alignment.CenterHorizontally) {
            LangBtn(stringResource(R.string.btn_indonesia)) {
                activity?.let {
                    AppPreferencesRepository.saveLanguage(it, "id-ID")
                    AppPreferencesRepository.applySavedLanguage(it)
                    it.recreate()
                }
            }
            Spacer(Modifier.height(16.dp))
            LangBtn(stringResource(R.string.btn_english)) {
                activity?.let {
                    AppPreferencesRepository.saveLanguage(it, "en-US")
                    AppPreferencesRepository.applySavedLanguage(it)
                    it.recreate()
                }
            }
        }
    }
}

@Composable
fun LangBtn(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Text(text)
    }
}