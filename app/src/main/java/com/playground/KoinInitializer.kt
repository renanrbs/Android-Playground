package com.playground

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        startKoin {
            androidContext(context)
            modules(dataModule)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf()

}