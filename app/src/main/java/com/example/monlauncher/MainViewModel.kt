package com.example.monlauncher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.monlauncher.data.apps.AppsRepository
import com.example.monlauncher.data.prefs.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val appsRepo = AppsRepository(app.packageManager)
    private val settingsRepo = SettingsRepository(app)

    private val _allApps = MutableStateFlow<List<AppEntry>>(emptyList())
    val allApps: StateFlow<List<AppEntry>> = _allApps

    val pinnedPackages: StateFlow<List<String>> =
        settingsRepo.pinnedPackages.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        refreshApps()
    }

    fun refreshApps() {
        viewModelScope.launch {
            _allApps.value = appsRepo.loadApps()
        }
    }

    fun savePinned(packages: List<String>) {
        viewModelScope.launch {
            settingsRepo.savePinned(packages)
        }
    }
}
