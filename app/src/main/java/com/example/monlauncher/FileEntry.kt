package com.example.monlauncher

import java.io.File

data class FileEntry(
    val name: String,
    val file: File,
    val isDirectory: Boolean
)
