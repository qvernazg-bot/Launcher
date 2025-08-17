package com.example.monlauncher.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.monlauncher.AppEntry
import com.example.monlauncher.MainViewModel

sealed interface SettingsDestination {
    object Home : SettingsDestination
    object HomeScreen : SettingsDestination
    object Folders : SettingsDestination
    object LookFeel : SettingsDestination
    object Integrations : SettingsDestination
    object SelectDefaultLauncher : SettingsDestination
    object About : SettingsDestination
    object Discord : SettingsDestination
}

@Composable
fun SettingsRoot(
    vm: MainViewModel,
    allApps: List<AppEntry>,
    pinned: List<String>,
    onSave: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val (destination, setDestination) = remember { mutableStateOf<SettingsDestination>(SettingsDestination.Home) }

    when (destination) {
        SettingsDestination.Home ->
            SettingsHomeScreen(onNavigate = setDestination, modifier = modifier)
        SettingsDestination.HomeScreen ->
            HomeScreenSettingsScreen(allApps = allApps, pinned = pinned, onSave = onSave, modifier = modifier)
        SettingsDestination.Folders ->
            FoldersSettingsScreen(vm = vm, allApps = allApps, modifier = modifier)
        SettingsDestination.LookFeel ->
            LookAndFeelSettingsScreen()
        SettingsDestination.Integrations ->
            IntegrationsSettingsScreen()
        SettingsDestination.SelectDefaultLauncher ->
            SelectDefaultLauncherScreen()
        SettingsDestination.About ->
            AboutScreen()
        SettingsDestination.Discord ->
            DiscordScreen()
    }
}
