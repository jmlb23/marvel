package com.github.jmlb23.android

import android.app.Application
import com.github.jmlb23.marvel.data.DIData
import com.github.jmlb23.marvel.presentation.DIPresentation
import org.koin.core.context.startKoin
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(DIData, DIPresentation)
        }
    }
}