package com.playground

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.playground.databinding.ActivityDatastoreBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class DataStoreActivity : AppCompatActivity() {

    private val sharedPrefs: SharedPreferences by inject()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SHARED_PREFS_NAME,
        produceMigrations = ::sharedPreferencesMigration
    )

    private fun sharedPreferencesMigration(context: Context) =
        listOf(SharedPreferencesMigration(context, SHARED_PREFS_NAME))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDatastoreBinding.inflate(layoutInflater)
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
            .apply()
    }

    fun readSharedPrefs(binding: ActivityDatastoreBinding) {
        binding.value.text = sharedPrefs.getString(SHARED_PREFS_KEY, "")
    }

    suspend fun saveDataStore() {
        dataStore.edit { settings ->
            val key = stringPreferencesKey(SHARED_PREFS_KEY)
            settings[key] = "$SHARED_PREFS_VALUE datastore"
        }
    }

    suspend fun readDataStore(binding: ActivityDatastoreBinding) {
        val key = stringPreferencesKey(SHARED_PREFS_KEY)
        val value: Flow<String> = dataStore.data
            .map { preferences ->
                preferences[key] ?: ""
            }

        binding.value.text = value.first()
    }


    companion object {
        const val SHARED_PREFS_KEY = "key"
        const val SHARED_PREFS_VALUE = "value"
    }
}