package com.example.monlauncher.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monlauncher.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: MainViewModel,
    onLaunch: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    val allApps by vm.allApps.collectAsStateWithLifecycle()
    val pinned by vm.pinnedPackages.collectAsStateWithLifecycle()

    val toShow = remember(allApps, pinned) {
        if (pinned.isEmpty()) allApps
        else pinned.mapNotNull { pkg -> allApps.find { it.packageName == pkg } }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MonLauncher") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Réglages")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { inner ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            RadialDock(
                apps = toShow,
                onLaunch = onLaunch,
                pageSize = 10
            )
        }
    }
}
