package com.example.hasscontrolsprovider

import android.app.Application
import timber.log.Timber

class HassControlsProviderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}