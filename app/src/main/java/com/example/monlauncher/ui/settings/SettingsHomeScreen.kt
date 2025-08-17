package com.example.monlauncher.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
//import com.example.monlauncher.BuildConfig

/**
 * Home page for settings showing the list of available categories and information about the app.
 */
@Composable
fun SettingsHomeScreen(
    onNavigate: (SettingsDestination) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val search = remember { mutableStateOf(TextFieldValue("")) }
    val items = listOf(
        SettingsItem(
            title = "Home screen",
            description = "Icon layout, set wheel, dock settings ...",
            destination = SettingsDestination.HomeScreen
        ),
        SettingsItem(
            title = "Folders",
            description = "Set window styles, background colors, icon layout",
            destination = SettingsDestination.Folders
        ),
        SettingsItem(
            title = "Look & Feel",
            description = null,
            destination = SettingsDestination.LookFeel
        ),
        SettingsItem(
            title = "Integrations",
            description = null,
            destination = SettingsDestination.Integrations
        )
    )

    SettingsPage(title = "Réglages", onBack = onBack) { inner ->
    LazyColumn(modifier.fillMaxSize().padding(inner).padding(horizontal = 16.dp)) {
        item {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "MonLauncher",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = search.value,
                onValueChange = { search.value = it },
                placeholder = { Text("Search settings") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
        }
        items(items) { entry ->
            SettingsRow(
                title = entry.title,
                description = entry.description,
                onClick = { onNavigate(entry.destination) }
            )
        }
        item {
            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))
            Text(
                //text = "MonLauncher ${BuildConfig.VERSION_NAME}",
                text = "MonLauncher 1.0",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        item {
            SettingsRow(
                title = "Select default launcher",
                onClick = { onNavigate(SettingsDestination.SelectDefaultLauncher) }
            )
        }
        item {
            SettingsRow(
                title = "About",
                onClick = { onNavigate(SettingsDestination.About) }
            )
        }
        item {
            SettingsRow(
                title = "Discord",
                onClick = { onNavigate(SettingsDestination.Discord) }
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
    }
}

private data class SettingsItem(
    val title: String,
    val description: String?,
    val destination: SettingsDestination
)

@Composable
private fun SettingsRow(
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (description != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
