package com.eventcheck.data

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FileStorageManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    suspend fun saveToDownloads(
        body: ResponseBody,
        fileName: String,
        mimeType: String
    ): Uri = withContext(Dispatchers.IO) {

        body.use { responseBody ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val resolver = appContext.contentResolver

                val values = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        fileName
                    )
                    put(
                        MediaStore.MediaColumns.MIME_TYPE,
                        mimeType
                    )
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS
                    )
                    put(
                        MediaStore.MediaColumns.IS_PENDING,
                        1
                    )
                }

                val uri = resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                ) ?: throw IOException(
                    "Could not create file in Downloads"
                )

                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        responseBody.byteStream().copyTo(outputStream)
                    } ?: throw IOException(
                        "Could not open output stream"
                    )

                    values.clear()
                    values.put(
                        MediaStore.MediaColumns.IS_PENDING,
                        0
                    )

                    resolver.update(
                        uri,
                        values,
                        null,
                        null
                    )

                    uri

                } catch (e: Exception) {
                    resolver.delete(uri, null, null)
                    throw e
                }

            } else {

                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )

                downloadsDir.mkdirs()

                val file = File(
                    downloadsDir,
                    fileName
                )

                file.outputStream().use { outputStream ->
                    responseBody.byteStream().copyTo(outputStream)
                }

                MediaScannerConnection.scanFile(
                    appContext,
                    arrayOf(file.absolutePath),
                    arrayOf(mimeType),
                    null
                )

                FileProvider.getUriForFile(
                    appContext,
                    "${appContext.packageName}.fileprovider",
                    file
                )
            }
        }
    }
}