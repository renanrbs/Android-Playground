package com.playground

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import org.koin.dsl.module


const val SHARED_PREFS_NAME = "shared prefs"

val dataModule = module {
    single<SharedPreferences> {
        val context: Context = get()
        context.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE)
    }
}