package com.example.monlauncher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.monlauncher.data.apps.AppsRepository
import com.example.monlauncher.data.prefs.SettingsRepository
import com.example.monlauncher.data.prefs.LookFeelSettings
import com.example.monlauncher.data.folders.Folder
import com.example.monlauncher.data.folders.FoldersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val appsRepo = AppsRepository(app.packageManager)
    private val settingsRepo = SettingsRepository(app)
    private val foldersRepo = FoldersRepository(app)

    private val _allApps = MutableStateFlow<List<AppEntry>>(emptyList())
    val allApps: StateFlow<List<AppEntry>> = _allApps

    val pinnedPackages: StateFlow<List<String>> =
        settingsRepo.pinnedPackages.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val lookFeel: StateFlow<LookFeelSettings> =
        settingsRepo.lookFeel.stateIn(viewModelScope, SharingStarted.Eagerly, LookFeelSettings(true, false))

    val folders: StateFlow<List<Folder>> =
        foldersRepo.folders.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

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

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setDarkTheme(enabled) }
    }

    fun setLargeText(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setLargeText(enabled) }
    }

    fun upsertFolder(folder: Folder) {
        viewModelScope.launch { foldersRepo.upsert(folder) }
    }

    fun deleteFolder(id: String) {
        viewModelScope.launch { foldersRepo.delete(id) }
    }

    fun assignAppToFolder(packageName: String, folderId: String) {
        viewModelScope.launch { foldersRepo.assignAppToFolder(packageName, folderId) }
    }

    fun folderForPackage(packageName: String): Folder? =
        folders.value.firstOrNull { packageName in it.apps }
}
