package com.example.monlauncher.data.apps

import android.content.Intent
import android.content.pm.PackageManager
import com.example.monlauncher.AppEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppsRepository(private val pm: PackageManager) {
    suspend fun loadApps(): List<AppEntry> = withContext(Dispatchers.IO) {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolves = pm.queryIntentActivities(intent, 0)
        resolves.map {
            val ai = it.activityInfo
            AppEntry(
                label = it.loadLabel(pm).toString(),
                packageName = ai.packageName,
                icon = it.loadIcon(pm)
            )
        }.sortedBy { it.label.lowercase() }
    }
}
