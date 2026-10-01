package com.eventcheck.presentation.scan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventcheck.R
import com.eventcheck.data.DataException
import com.eventcheck.data.EventDatasource
import com.eventcheck.data.response.CheckInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val eventDatasource: EventDatasource
) : ViewModel() {

    private val _isScanning = mutableStateOf(true)
    val isScanning: State<Boolean> = _isScanning

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _checkInResult = mutableStateOf<CheckInResponse?>(null)
    val checkInResult: State<CheckInResponse?> = _checkInResult

    private val _errorResId = mutableStateOf<Int?>(null)
    val errorResId: State<Int?> = _errorResId

    private val _lastScannedCode = mutableStateOf<String?>(null)

    fun onQrScanned(qrToken: String) {
        if (!_isScanning.value || _isLoading.value || qrToken == _lastScannedCode.value) return

        _lastScannedCode.value = qrToken
        _isScanning.value = false

        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            try {
                val response = eventDatasource.checkIn(qrToken)
                _checkInResult.value = response
            } catch (e: DataException) {
                _errorResId.value = when (e) {
                    is DataException.InvalidInput -> R.string.error_invalid_input
                    is DataException.CheckInFailed -> R.string.check_in_failed
                    is DataException.NetworkError -> R.string.error_network
                    else -> R.string.error_server_fallback
                }
            } catch (_: Exception) {
                _errorResId.value = R.string.error_unexpected
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetScanState() {
        _checkInResult.value = null
        _errorResId.value = null
        _lastScannedCode.value = null
        _isScanning.value = true
    }

    fun clearError() {
        _errorResId.value = null
        _isScanning.value = true
    }
}
