package com.example.modul3xml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.modul3xml.databinding.ActivityMainBinding
import com.example.modul3xml.util.LanguageManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageManager.applySavedLocale(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}