package com.example.monlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monlauncher.ui.home.HomeScreen
import com.example.monlauncher.ui.theme.MonLauncherTheme

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lookFeel by vm.lookFeel.collectAsStateWithLifecycle()
            MonLauncherTheme(darkTheme = lookFeel.darkTheme, largeText = lookFeel.largeText) {
                HomeScreen(
                    vm = vm,
                    onOpenSettings = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                )
            }
        }
    }

}
