package com.example.modul5compose.data.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object AppPreferencesRepository {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANG = "lang"

    private fun getPref(ctx: Context) = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLanguage(ctx: Context, lang: String) {
        getPref(ctx).edit().putString(KEY_LANG, lang).apply()
    }

    fun applySavedLanguage(ctx: Context) {
        val lang = getPref(ctx).getString(KEY_LANG, "id-ID") ?: "id-ID"
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
    }
}