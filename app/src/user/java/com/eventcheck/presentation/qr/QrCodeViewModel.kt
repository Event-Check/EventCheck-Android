package com.eventcheck.presentation.qr

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.eventcheck.util.QrCodeGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor() : ViewModel() {

    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
    val qrCodeBitmap: State<Bitmap?> = _qrCodeBitmap

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _registrationId = mutableStateOf("")
    val registrationId: State<String> = _registrationId

    fun setupData(name: String, email: String, registrationId: String, qrToken: String) {
        _name.value = name
        _email.value = email
        _registrationId.value = registrationId
        _qrCodeBitmap.value = QrCodeGenerator.generateQrCode(qrToken)
    }
}
