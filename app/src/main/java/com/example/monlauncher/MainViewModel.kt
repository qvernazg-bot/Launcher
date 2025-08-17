package com.example.monlauncher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.monlauncher.data.apps.AppsRepository
import com.example.monlauncher.data.prefs.SettingsRepository
import com.example.monlauncher.data.prefs.LookFeelSettings
import com.example.monlauncher.data.folders.Folder
import com.example.monlauncher.data.folders.FoldersRepository
import com.example.monlauncher.data.files.FilesRepository
import com.example.monlauncher.FileEntry
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val appsRepo = AppsRepository(app.packageManager)
    private val settingsRepo = SettingsRepository(app)
    private val foldersRepo = FoldersRepository(app)
    private val filesRepo = FilesRepository()

    /** Root directory containing user created files and folders. */
    val rootDir: File = app.filesDir

    private val _allApps = MutableStateFlow<List<AppEntry>>(emptyList())
    val allApps: StateFlow<List<AppEntry>> = _allApps

    val pinnedPackages: StateFlow<List<String>> =
        settingsRepo.pinnedPackages.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val lookFeel: StateFlow<LookFeelSettings> =
        settingsRepo.lookFeel.stateIn(viewModelScope, SharingStarted.Eagerly, LookFeelSettings(true, false))

    val folders: StateFlow<List<Folder>> =
        foldersRepo.folders.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _currentDir = MutableStateFlow<File>(rootDir)
    val currentDir: StateFlow<File> = _currentDir

    private val _files = MutableStateFlow<List<FileEntry>>(emptyList())
    val files: StateFlow<List<FileEntry>> = _files

    init {
        refreshApps()
        refreshFiles()
    }

    fun refreshApps() {
        viewModelScope.launch {
            _allApps.value = appsRepo.loadApps()
        }
    }

    fun refreshFiles() {
        viewModelScope.launch {
            _files.value = filesRepo.listFiles(_currentDir.value)
        }
    }

    fun open(entry: FileEntry) {
        if (entry.isDirectory) {
            _currentDir.value = entry.file
            refreshFiles()
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
