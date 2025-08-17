package com.example.monlauncher.data.files

import java.io.File

class FileRepository(private val root: File = File("/")) {
    fun children(dir: File): List<File> =
        dir.listFiles()?.sortedBy { it.name.lowercase() }?.toList() ?: emptyList()

    val rootDir: File get() = root
}

