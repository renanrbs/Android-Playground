package com.playground

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.koin.core.qualifier.named
import org.koin.dsl.module


const val SHARED_PREFS_NAME = "shared prefs"
private const val secureSharedPreferencesName = "secureSharedPrefs"
private const val masterKeyAlias = "SecureActivityMasterKeyAlias"


val dataModule = module {
    single<SharedPreferences> {
        val context: Context = get()
        context.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE)
    }

    single<MasterKey> {
        MasterKey.Builder(get(), masterKeyAlias)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    single<SharedPreferences>(named("encrypted")) {
        EncryptedSharedPreferences.create(
            get(),
            secureSharedPreferencesName,
            get<MasterKey>(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}