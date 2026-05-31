package com.example.modul5compose.data.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import timber.log.Timber

object AppPreferencesRepository {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_LANGUAGE = "language"
    private const val DEFAULT_LANGUAGE = "id-ID"

    private fun pref(context: Context) = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getLanguage(context: Context): String {
        val lang = pref(context).getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
        Timber.d("getLanguage: $lang")
        return lang
    }

    fun saveLanguage(context: Context, language: String) {
        Timber.d("saveLanguage: $language")
        pref(context).edit().putString(KEY_LANGUAGE, language).commit()
        Timber.d("saveLanguage: commit done")
    }

    fun applySavedLanguage(context: Context) {
        val language = getLanguage(context)
        Timber.d("applySavedLanguage: applying locale $language")
        try {
            val localeList = LocaleListCompat.forLanguageTags(language)
            Timber.d("applySavedLanguage: localeList created: $localeList")
            AppCompatDelegate.setApplicationLocales(localeList)
            Timber.d("applySavedLanguage: successfully applied locale")
        } catch (e: Exception) {
            Timber.e("applySavedLanguage error: ${e.message}")
            e.printStackTrace()
        }
    }
}