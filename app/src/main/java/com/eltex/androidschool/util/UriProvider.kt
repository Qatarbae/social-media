package com.eltex.androidschool.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class UriProvider @Inject constructor(@ApplicationContext private val context: Context) {

    fun getPhotoUri(): Uri {
        val directory = context.cacheDir.resolve("file_picker").apply {
            mkdirs()
        }
        val file = File(directory, "image.png")

        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
    }

    fun getVideoUri(): Uri {
        val directory = context.cacheDir.resolve("file_picker").apply {
            mkdirs()
        }
        val file = File(directory, "video.mp4")

        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
    }
}
