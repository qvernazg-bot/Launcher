package com.example.monlauncher.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monlauncher.AppEntry
import com.example.monlauncher.MainViewModel
import com.example.monlauncher.R
import com.example.monlauncher.data.folders.Folder

/** Settings screen to manage application folders. */
@Composable
fun FoldersSettingsScreen(
    vm: MainViewModel,
    allApps: List<AppEntry>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val folders by vm.folders.collectAsStateWithLifecycle()
    var editing by remember { mutableStateOf<Folder?>(null) }

    SettingsPage(
        title = "Folders",
        onBack = onBack,
        floatingActionButton = {
            FloatingActionButton(onClick = { editing = Folder(name = "") }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_custom),
                    contentDescription = "Add folder",
                )
            }
        }
    ) { inner ->
        LazyColumn(
            modifier
                .padding(inner)
                .fillMaxSize(),
        ) {
            items(folders) { folder ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { editing = folder }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(folder.name, style = MaterialTheme.typography.titleMedium)
                }
            }
            item { Spacer(Modifier.height(80.dp)) } // space for FAB
        }
    }

    editing?.let { folder ->
        FolderEditDialog(
            initial = folder,
            allApps = allApps,
            onSave = { updated ->
                vm.upsertFolder(updated)
                updated.apps.forEach { pkg -> vm.assignAppToFolder(pkg, updated.id) }
                editing = null
            },
            onDelete = if (folders.any { it.id == folder.id }) {
                { vm.deleteFolder(folder.id); editing = null }
            } else null,
            onDismiss = { editing = null }
        )
    }
}
