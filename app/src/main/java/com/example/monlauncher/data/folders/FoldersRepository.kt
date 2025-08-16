package com.example.monlauncher.data.folders

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.monlauncher.data.prefs.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val FOLDERS_KEY = stringPreferencesKey("folders_json")

/** Repository that persists launcher folders using DataStore. */
class FoldersRepository(private val context: Context) {
    private val dataStore = context.dataStore

    /** Flow emitting the current list of folders. */
    val folders: Flow<List<Folder>> = dataStore.data.map { prefs ->
        prefs[FOLDERS_KEY]?.let { decode(it) } ?: emptyList()
    }

    suspend fun save(folders: List<Folder>) {
        dataStore.edit { it[FOLDERS_KEY] = encode(folders) }
    }

    suspend fun upsert(folder: Folder) {
        val current = folders.first()
        val updated = current.filterNot { it.id == folder.id } + folder
        save(updated)
    }

    suspend fun delete(id: String) {
        val current = folders.first()
        save(current.filterNot { it.id == id })
    }

    suspend fun assignAppToFolder(packageName: String, folderId: String) {
        val current = folders.first().map { folder ->
            if (folder.id == folderId) {
                folder.copy(apps = (folder.apps + packageName).distinct())
            } else {
                folder.copy(apps = folder.apps - packageName)
            }
        }
        save(current)
    }

    fun folderForPackage(packageName: String): Flow<Folder?> =
        folders.map { list -> list.firstOrNull { packageName in it.apps } }

    private fun encode(list: List<Folder>): String {
        val array = JSONArray()
        list.forEach { f ->
            val obj = JSONObject()
            obj.put("id", f.id)
            obj.put("name", f.name)
            f.color?.let { obj.put("color", it) }
            f.icon?.let { obj.put("icon", it) }
            obj.put("apps", JSONArray(f.apps))
            f.position?.let { obj.put("position", it) }
            array.put(obj)
        }
        return array.toString()
    }

    private fun decode(json: String): List<Folder> {
        val array = JSONArray(json)
        return List(array.length()) { i ->
            val obj = array.getJSONObject(i)
            Folder(
                id = obj.getString("id"),
                name = obj.getString("name"),
                color = if (obj.has("color")) obj.getLong("color") else null,
                icon = if (obj.has("icon")) obj.getString("icon") else null,
                apps = buildList {
                    val arr = obj.getJSONArray("apps")
                    for (j in 0 until arr.length()) add(arr.getString(j))
                },
                position = if (obj.has("position")) obj.getInt("position") else null,
            )
        }
    }
}
