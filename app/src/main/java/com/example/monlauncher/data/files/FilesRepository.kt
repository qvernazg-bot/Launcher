package com.example.monlauncher.data.files

import com.example.monlauncher.FileEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FilesRepository {
    suspend fun listFiles(dir: File): List<FileEntry> = withContext(Dispatchers.IO) {
        dir.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            ?.map { file ->
                FileEntry(
                    name = file.name,
                    file = file,
                    isDirectory = file.isDirectory
                )
            } ?: emptyList()
    }
}
