@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.monlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monlauncher.ui.settings.SettingsRoot
import com.example.monlauncher.ui.theme.MonLauncherTheme
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonLauncherTheme {
                val allApps by vm.allApps.collectAsStateWithLifecycle()
                val pinned by vm.pinnedPackages.collectAsStateWithLifecycle()
                val scope = rememberCoroutineScope()

                Scaffold(
                    topBar = { TopAppBar(title = { Text("Réglages") }) }
                ) { inner ->
                    SettingsRoot(
                        allApps = allApps,
                        pinned = pinned,
                        onSave = { list ->
                            scope.launch { vm.savePinned(list) }
                            finish()
                        },
                        modifier = Modifier.padding(inner)
                    )
                }
            }
        }
    }
}
