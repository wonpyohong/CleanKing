package com.wonpyohong.android.cleanking

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.startKoin

class CleanApplication: Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: CleanApplication

        val applicationContext: Context
            get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        Stetho.initializeWithDefaults(this)

        startKoin(this, listOf(koinModule))
    }
}