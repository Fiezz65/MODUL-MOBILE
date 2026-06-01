package com.example.modul5compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.modul5compose.data.datastore.AppPreferencesRepository
import com.example.modul5compose.ui.navigation.AppNavigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferencesRepository.applySavedLanguage(this)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = Color.Black)) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    AppNavigation()
                }
            }
        }
    }
}