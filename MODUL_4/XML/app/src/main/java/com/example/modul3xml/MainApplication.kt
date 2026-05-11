package com.example.modul3xml

import android.app.Application
import android.content.pm.ApplicationInfo
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
