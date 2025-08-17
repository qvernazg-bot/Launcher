package com.example.monlauncher.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "monlauncher_prefs")

class SettingsRepository(private val context: Context) {
    private val dataStore = context.dataStore

    val pinnedPackages: Flow<List<String>> = dataStore.data.map { prefs ->
        prefs[PINS]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    }

    val lookFeel: Flow<LookFeelSettings> = dataStore.data.map { prefs ->
        LookFeelSettings(
            darkTheme = prefs[DARK_THEME] ?: true,
            largeText = prefs[LARGE_TEXT] ?: false
        )
    }

    suspend fun savePinned(packages: List<String>) {
        dataStore.edit { it[PINS] = packages.joinToString(",") }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { it[DARK_THEME] = enabled }
    }

    suspend fun setLargeText(enabled: Boolean) {
        dataStore.edit { it[LARGE_TEXT] = enabled }
    }
}

data class LookFeelSettings(
    val darkTheme: Boolean,
    val largeText: Boolean
)
