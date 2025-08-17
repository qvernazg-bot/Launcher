package com.example.monlauncher.data.folders

import java.util.UUID

/**
 * Represents a folder grouping applications inside the launcher.
 *
 * @param id unique identifier. Generated automatically when not provided.
 * @param name display name of the folder.
 * @param color ARGB color stored as a [Long]. Can be null for default color.
 * @param icon textual representation of the icon (could be an emoji or resource name).
 * @param apps list of package names assigned to this folder.
 * @param position TODO: not yet implemented.
 */
 data class Folder(
     val id: String = UUID.randomUUID().toString(),
     val name: String,
     val color: Long? = null,
     val icon: String? = null,
     val apps: List<String> = emptyList(),
     // TODO: handle folder position when layout system is implemented
     val position: Int? = null,
 )
