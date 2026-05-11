package com.example.modul3xml.util

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat

object LanguageManager {

    private const val PREF_NAME = "app_settings"
    private const val KEY_LANG = "selected_language"

    fun applySavedLocale(context: Context) {
        val savedLanguage = getSavedLanguage(context)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(savedLanguage))
    }

    fun setLocale(activity: Activity, languageCode: String) {
        saveLanguage(activity, languageCode)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }

    fun isIndonesian(context: Context): Boolean {
        val language = context.resources.configuration.locales[0].language
        return language == "id" || language == "in"
    }

    private fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANG, "id") ?: "id"
    }

    private fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_LANG, languageCode)
        }
    }
}