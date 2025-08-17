package com.example.monlauncher.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(onBack: () -> Unit) {
    SettingsPage(title = "About", onBack = onBack) { inner: PaddingValues ->
        Text(
            "About MonLauncher",
            modifier = Modifier.padding(inner).padding(16.dp)
        )
    }
}
