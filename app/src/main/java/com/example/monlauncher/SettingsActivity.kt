package com.example.monlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monlauncher.ui.settings.SettingsRoot
import com.example.monlauncher.ui.theme.MonLauncherTheme
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lookFeel by vm.lookFeel.collectAsStateWithLifecycle()
            MonLauncherTheme(darkTheme = lookFeel.darkTheme, largeText = lookFeel.largeText) {
                val allApps by vm.allApps.collectAsStateWithLifecycle()
                val pinned by vm.pinnedPackages.collectAsStateWithLifecycle()
                val scope = rememberCoroutineScope()
                SettingsRoot(
                    vm = vm,
                    allApps = allApps,
                    pinned = pinned,
                    onSave = { list ->
                        scope.launch { vm.savePinned(list) }
                        finish()
                    },
                    onClose = { finish() }
                )
            }
        }
    }
}
