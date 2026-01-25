package com.watermuffin.todaylist.ui.screens.profile.helpers

import android.content.Context
import android.net.Uri
import java.io.File

fun saveAvatarToStorage(context: Context, imageUri: Uri): String? {
    return try {
        val timestamp = System.currentTimeMillis()
        val fileName = "avatar_$timestamp.jpg"

        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val file = File(context.filesDir, fileName)

            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            fileName
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}