package com.eventcheck.presentation.scan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventcheck.R
import com.eventcheck.data.DataException
import com.eventcheck.data.EventRepository
import com.eventcheck.data.response.CheckInResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: EventRepository
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
        if (!_isScanning.value || _isLoading.value || _checkInResult.value != null || qrToken == _lastScannedCode.value) return

        _lastScannedCode.value = qrToken
        _isScanning.value = false

        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            try {
                val response = repository.checkIn(qrToken)
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
        _isScanning.value = true

        // Debounce clearing lastScannedCode to avoid immediate duplicate scanning of the same physical QR code
        viewModelScope.launch {
            delay(2500L)
            if (_checkInResult.value == null) {
                _lastScannedCode.value = null
            }
        }
    }

    fun stopScanning() {
        _isScanning.value = false
        _checkInResult.value = null
        _errorResId.value = null
    }

    fun clearError() {
        _errorResId.value = null
        _isScanning.value = true
    }
}
