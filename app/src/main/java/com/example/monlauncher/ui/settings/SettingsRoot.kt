package com.example.monlauncher.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.monlauncher.AppEntry

@Composable
fun SettingsRoot(
    allApps: List<AppEntry>,
    pinned: List<String>,
    onSave: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    GeneralSettingsScreen(allApps = allApps, pinned = pinned, onSave = onSave, modifier = modifier)
}
