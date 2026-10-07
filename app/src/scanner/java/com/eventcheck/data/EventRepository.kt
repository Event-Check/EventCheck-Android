package com.eventcheck.data

import android.net.Uri
import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.data.response.StatsResponse
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventDatasource: EventDatasource,
    private val fileStorageManager: FileStorageManager
) {
     suspend fun getStats(): StatsResponse =  eventDatasource.getStats()
     suspend fun exportReport(
        format: String,
        includeAttendees: Boolean,
        fileName: String,
        mimeType: String
    ): Uri {

        val body = eventDatasource.exportReport(
            format = format,
            includeAttendees = includeAttendees
        )

        return fileStorageManager.saveToDownloads(
            body = body,
            fileName = fileName,
            mimeType = mimeType
        )
    }
    suspend fun checkIn(qrToken: String): CheckInResponse = eventDatasource.checkIn(qrToken)
}