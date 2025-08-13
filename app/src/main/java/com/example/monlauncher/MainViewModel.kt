package com.example.monlauncher

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val pm: PackageManager = app.packageManager
    private val dataStore = app.dataStore

    private val _allApps = MutableStateFlow<List<AppEntry>>(emptyList())
    val allApps: StateFlow<List<AppEntry>> = _allApps

    private val PINS = stringPreferencesKey("pins_csv")
    val pinnedPackages: StateFlow<List<String>> =
        dataStore.data.map { prefs ->
            prefs[PINS]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        refreshApps()
    }

    fun refreshApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val resolves = pm.queryIntentActivities(intent, 0)
            val apps = resolves.map {
                val ai = it.activityInfo
                AppEntry(
                    label = it.loadLabel(pm).toString(),
                    packageName = ai.packageName,
                    icon = it.loadIcon(pm)
                )
            }.sortedBy { it.label.lowercase() }
            _allApps.value = apps
        }
    }

    fun savePinned(packages: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { it[PINS] = packages.joinToString(",") }
        }
    }
}
