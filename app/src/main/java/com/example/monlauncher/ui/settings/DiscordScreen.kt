package com.example.monlauncher.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiscordScreen(onBack: () -> Unit) {
    SettingsPage(title = "Discord", onBack = onBack) { inner: PaddingValues ->
        Text(
            "Join us on Discord",
            modifier = Modifier.padding(inner).padding(16.dp)
        )
    }
}
