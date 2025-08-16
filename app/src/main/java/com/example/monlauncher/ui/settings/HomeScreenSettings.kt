package com.example.monlauncher.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.monlauncher.AppEntry

@Composable
fun HomeScreenSettingsScreen(
    allApps: List<AppEntry>,
    pinned: List<String>,
    onSave: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember(pinned) { mutableStateOf(pinned.toMutableList()) }

    Column(modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(allApps, key = { it.packageName }) { app ->
                val checked = selected.contains(app.packageName)
                val bmp = remember(app.packageName) { app.icon.toBitmap(96, 96).asImageBitmap() }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (checked) selected.remove(app.packageName) else selected.add(app.packageName)
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = bmp,
                        contentDescription = app.label,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(app.label, Modifier.weight(1f))
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            if (it) selected.add(app.packageName) else selected.remove(app.packageName)
                        }
                    )
                }
            }
        }
        Button(
            onClick = { onSave(selected) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text("Enregistrer") }
    }
}
