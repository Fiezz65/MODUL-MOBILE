package com.example.modul3compose.ui.screen

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.modul3compose.R
import java.util.Locale

@Composable
fun SettingsScreen(navController: androidx.navigation.NavController) {
    val act = LocalActivity.current
    val blue = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))

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
        Column(
            modifier = Modifier.fillMaxSize().padding(p),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LanguageButton(stringResource(R.string.btn_indonesia), blue) { act?.let { updateLang(it, "id") } }
            Spacer(Modifier.height(16.dp))
            LanguageButton(stringResource(R.string.btn_english), blue) { act?.let { updateLang(it, "en") } }
        }
    }
}

@Composable
fun LanguageButton(text: String, colors: ButtonColors, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        colors = colors
    ) {
        Text(text)
    }
}

@Suppress("DEPRECATION")
private fun updateLang(act: Activity, code: String) {
    val locale = Locale(code)
    Locale.setDefault(locale)
    val config = Configuration(act.resources.configuration)
    config.setLocale(locale)
    act.resources.updateConfiguration(config, act.resources.displayMetrics)
    act.recreate()
}