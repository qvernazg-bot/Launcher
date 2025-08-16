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

    suspend fun savePinned(packages: List<String>) {
        dataStore.edit { it[PINS] = packages.joinToString(",") }
    }
}
