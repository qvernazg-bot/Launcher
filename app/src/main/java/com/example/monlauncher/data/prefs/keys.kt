package com.example.monlauncher.data.prefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val PINS = stringPreferencesKey("pins_csv")
val DARK_THEME = booleanPreferencesKey("dark_theme")
val LARGE_TEXT = booleanPreferencesKey("large_text")
