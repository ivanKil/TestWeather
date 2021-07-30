package com.lessons.weatherappandroid

import android.app.Application
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger()
            fragmentFactory()
            //androidContext(this@App)
            modules(baseModule)
        }
    }
}