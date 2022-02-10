package com.playground

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.playground.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val sharedPrefs by lazy {
        getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SHARED_PREFS_NAME,
        produceMigrations = ::sharedPreferencesMigration
    )

    private fun sharedPreferencesMigration(context: Context) =
        listOf(SharedPreferencesMigration(context, SHARED_PREFS_NAME))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.datastore.setOnClickListener {
            lifecycleScope.launch {
                saveDataStore()
                readDataStore(binding)
            }
        }
        binding.shared.setOnClickListener {
            saveSharedPrefs()
            readSharedPrefs(binding)
        }
        setContentView(binding.root)
    }

    fun saveSharedPrefs() {
        sharedPrefs.edit().putString(SHARED_PREFS_KEY, SHARED_PREFS_VALUE + " shared")
            .putString("biscoito", "bolacha")
            .apply()
    }

    fun readSharedPrefs(binding: ActivityMainBinding) {
        binding.value.text = sharedPrefs.getString(SHARED_PREFS_KEY, "")
    }

    suspend fun saveDataStore() {
        dataStore.edit { settings ->
            val key = stringPreferencesKey(SHARED_PREFS_KEY)
            settings[key] = "$SHARED_PREFS_VALUE datastore"
            settings[stringPreferencesKey("asdasd")] = "$SHARED_PREFS_VALUE storeadsda "
        }
    }

    suspend fun readDataStore(binding: ActivityMainBinding) {
        val key = stringPreferencesKey(SHARED_PREFS_KEY)
        val value: Flow<String> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[key] ?: ""
            }
        val value2: Flow<String> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[stringPreferencesKey("biscoito")] ?: ""
            }

        binding.value.text = value.first() + value2.first()
    }


    companion object {
        const val SHARED_PREFS_NAME = "shared prefs"
        const val SHARED_PREFS_KEY = "key"
        const val SHARED_PREFS_VALUE = "value"
    }
}