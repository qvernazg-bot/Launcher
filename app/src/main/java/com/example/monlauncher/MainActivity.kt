package com.example.monlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.monlauncher.ui.home.HomeScreen
import com.example.monlauncher.ui.theme.MonLauncherTheme

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonLauncherTheme {
                HomeScreen(
                    vm = vm,
                    onLaunch = { pkg -> launchPackage(pkg) },
                    onOpenSettings = {
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                )
            }
        }
    }

    private fun launchPackage(packageName: String) {
        val launch = packageManager.getLaunchIntentForPackage(packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (launch != null) startActivity(launch)
    }
}
