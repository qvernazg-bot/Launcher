package com.example.monlauncher.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectDefaultLauncherScreen(onBack: () -> Unit) {
    SettingsPage(title = "Select default launcher", onBack = onBack) { inner: PaddingValues ->
        Text(
            "Select default launcher",
            modifier = Modifier.padding(inner).padding(16.dp)
        )
    }
}
