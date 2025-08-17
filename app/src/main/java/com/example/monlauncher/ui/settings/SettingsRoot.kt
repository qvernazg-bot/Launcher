package com.example.monlauncher.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.monlauncher.AppEntry
import com.example.monlauncher.MainViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stack = remember { mutableStateListOf<SettingsDestination>(SettingsDestination.Home) }
    val current = stack.last()
    val navigate: (SettingsDestination) -> Unit = { stack.add(it) }
    val goBack: () -> Unit = {
        if (stack.size > 1) stack.removeAt(stack.lastIndex) else onClose()
    }

    BackHandler(onBack = goBack)

    when (current) {
        SettingsDestination.Home ->
            SettingsHomeScreen(onNavigate = navigate, onBack = goBack, modifier = modifier)
        SettingsDestination.HomeScreen ->
            HomeScreenSettingsScreen(allApps = allApps, pinned = pinned, onSave = onSave, onBack = goBack, modifier = modifier)
        SettingsDestination.Folders ->
            FoldersSettingsScreen(vm = vm, allApps = allApps, onBack = goBack, modifier = modifier)
        SettingsDestination.LookFeel -> {
            val lookFeel by vm.lookFeel.collectAsStateWithLifecycle()
            LookAndFeelSettingsScreen(
                darkTheme = lookFeel.darkTheme,
                largeText = lookFeel.largeText,
                onDarkThemeChange = vm::setDarkTheme,
                onLargeTextChange = vm::setLargeText,
                onBack = goBack
            )
        }
        SettingsDestination.Integrations ->
            IntegrationsSettingsScreen(onBack = goBack)
        SettingsDestination.SelectDefaultLauncher ->
            SelectDefaultLauncherScreen(onBack = goBack)
        SettingsDestination.About ->
            AboutScreen(onBack = goBack)
        SettingsDestination.Discord ->
            DiscordScreen(onBack = goBack)
    }
}
