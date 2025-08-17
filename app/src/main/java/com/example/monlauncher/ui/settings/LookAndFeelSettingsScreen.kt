package com.example.monlauncher.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LookAndFeelSettingsScreen(
    darkTheme: Boolean,
    largeText: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onLargeTextChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    SettingsPage(title = "Look & Feel", onBack = onBack) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Dark theme", modifier = Modifier.weight(1f))
                Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Large text", modifier = Modifier.weight(1f))
                Switch(checked = largeText, onCheckedChange = onLargeTextChange)
            }
        }
    }
}
