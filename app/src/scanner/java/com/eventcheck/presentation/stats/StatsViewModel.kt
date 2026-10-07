package com.eventcheck.presentation.stats

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventcheck.R
import com.eventcheck.data.DataException
import com.eventcheck.data.EventRepository
import com.eventcheck.data.response.StatsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ExportedFile(
    val uri: Uri,
    val name: String,
    val mimeType: String,
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isExporting = mutableStateOf(false)
    val isExporting: State<Boolean> = _isExporting

    private val _stats = mutableStateOf<StatsResponse?>(null)
    val stats: State<StatsResponse?> = _stats

    private val _includeAttendees = mutableStateOf(true)
    val includeAttendees: State<Boolean> = _includeAttendees

    private val _exportedFile = mutableStateOf<ExportedFile?>(null)
    val exportedFile: State<ExportedFile?> = _exportedFile

    private val _errorResId = mutableStateOf<Int?>(null)
    val errorResId: State<Int?> = _errorResId

    fun fetchStats() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            try {
                val response = repository.getStats()
                _stats.value = response
            } catch (e: DataException) {
                if (e is DataException.NetworkError) {
                    _errorResId.value = R.string.error_network
                } else {
                    _stats.value = null
                }
            } catch (_: Exception) {
                _stats.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleIncludeAttendees(include: Boolean) {
        _includeAttendees.value = include
    }

    fun exportReport(format: String) {
        viewModelScope.launch {
            _isExporting.value = true
            _errorResId.value = null
            _exportedFile.value = null

            try {
                val isPdf = format.equals("pdf", ignoreCase = true)

                val date = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
                ).format(Date())

                val fileName = if (isPdf) {
                    "attendance_report_$date.pdf"
                } else {
                    "attendance_report_$date.xlsx"
                }

                val mimeType = if (isPdf) {
                    "application/pdf"
                } else {
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                }

                val uri = repository.exportReport(
                    format = format,
                    includeAttendees = _includeAttendees.value,
                    fileName = fileName,
                    mimeType = mimeType
                )

                _exportedFile.value = ExportedFile(
                    uri = uri,
                    name = fileName,
                    mimeType = mimeType
                )

            } catch (e: DataException) {
                _errorResId.value = when (e) {
                    is DataException.NetworkError ->
                        R.string.error_network

                    else ->
                        R.string.error_server_fallback
                }

            } catch (_: Exception) {
                _errorResId.value = R.string.error_unexpected

            } finally {
                _isExporting.value = false
            }
        }
    }

    fun onExportHandled() {
        _exportedFile.value = null
    }

    fun clearError() {
        _errorResId.value = null
    }
}