package com.example.monlauncher.ui.settings

import androidx.compose.foundation.border
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.res.painterResource
import com.example.monlauncher.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    title: String,
    onBack: () -> Unit,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.primary),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Add folder",
                        )
                    }
                }
            )
        },
        floatingActionButton = {floatingActionButton?.invoke()},
        containerColor = MaterialTheme.colorScheme.background,
        content = { innerPadding ->
            content(innerPadding)
        }
    )
}
