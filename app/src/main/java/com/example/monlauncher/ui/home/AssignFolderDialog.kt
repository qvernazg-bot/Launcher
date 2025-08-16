package com.example.monlauncher.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monlauncher.data.folders.Folder

/** Dialog prompting the user to assign an application to a folder. */
@Composable
fun AssignFolderDialog(
    folders: List<Folder>,
    onAssign: (Folder) -> Unit,
    onCancel: () -> Unit,
) {
    var selected by remember { mutableStateOf<Folder?>(null) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Assign to folder") },
        text = {
            Column {
                folders.forEach { folder ->
                    val selectedOption = selected?.id == folder.id
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedOption,
                                onClick = { selected = folder }
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedOption,
                            onClick = { selected = folder }
                        )
                        Text(folder.name, Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { selected?.let(onAssign) },
                enabled = selected != null
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancel") }
        }
    )
}
