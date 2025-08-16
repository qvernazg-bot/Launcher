package com.example.monlauncher.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monlauncher.AppEntry
import com.example.monlauncher.data.folders.Folder

/** Dialog used to create or edit a [Folder]. */
@Composable
fun FolderEditDialog(
    initial: Folder,
    allApps: List<AppEntry>,
    onSave: (Folder) -> Unit,
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(initial.name) }
    var icon by remember { mutableStateOf(initial.icon ?: "") }
    var color by remember { mutableStateOf(initial.color?.toString() ?: "") }
    val selected = remember { mutableStateOf(initial.apps.toMutableSet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Folder") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = icon,
                    onValueChange = { icon = it },
                    label = { Text("Icon") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color (ARGB)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text("Position: TODO")
                Spacer(Modifier.height(8.dp))
                Text("Apps:")
                LazyColumn(Modifier.heightIn(max = 200.dp)) {
                    items(allApps) { app ->
                        val checked = selected.value.contains(app.packageName)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) selected.value.add(app.packageName)
                                    else selected.value.remove(app.packageName)
                                }
                            )
                            Text(app.label, Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val colorLong = color.toLongOrNull()
                val folder = initial.copy(
                    name = name,
                    icon = icon.ifBlank { null },
                    color = colorLong,
                    apps = selected.value.toList()
                )
                onSave(folder)
            }) { Text("Save") }
        },
        dismissButton = {
            Row {
                onDelete?.let { del ->
                    TextButton(onClick = del) { Text("Delete") }
                }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}
