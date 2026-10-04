package com.eventcheck.presentation.verify

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventcheck.R
import com.eventcheck.data.DataException
import com.eventcheck.data.EventDatasource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface VerifyUiEffect {
    data class NavigateToQr(
        val name: String,
        val email: String,
        val registrationId: String,
        val qrToken: String,
    ) : VerifyUiEffect
}

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val eventDatasource: EventDatasource
) : ViewModel() {

    private var name = ""
    private var email = ""
    private var registrationId = ""
    private var isInitialized = false
    private var cooldownJob: Job? = null

    private val _code = mutableStateOf("")
    val code: State<String> = _code

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isResending = mutableStateOf(false)
    val isResending: State<Boolean> = _isResending

    private val _errorResId = mutableStateOf<Int?>(null)
    val errorResId: State<Int?> = _errorResId

    private val _infoResId = mutableStateOf<Int?>(null)
    val infoResId: State<Int?> = _infoResId

    private val _resendSecondsLeft = mutableIntStateOf(0)
    val resendSecondsLeft: State<Int> = _resendSecondsLeft

    private val _effect = Channel<VerifyUiEffect>(Channel.BUFFERED)
    val effect: Flow<VerifyUiEffect> = _effect.receiveAsFlow()

    val isCodeComplete = derivedStateOf { _code.value.length == CODE_LENGTH }

    fun setup(name: String, email: String, registrationId: String) {
        if (isInitialized && this.email == email) return
        reset()
        this.name = name
        this.email = email
        this.registrationId = registrationId
        isInitialized = true
        startResendCooldown()
    }

    fun reset() {
        cooldownJob?.cancel()
        isInitialized = false
        _code.value = ""
        _isLoading.value = false
        _isResending.value = false
        _errorResId.value = null
        _infoResId.value = null
        _resendSecondsLeft.intValue = 0
    }

    fun onCodeChange(newCode: String) {
        _code.value = newCode.filter { it.isDigit() }.take(CODE_LENGTH)
        _errorResId.value = null
        _infoResId.value = null
    }

    fun verify() {
        if (!isCodeComplete.value || _isLoading.value || _isResending.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            _infoResId.value = null
            try {
                val response = eventDatasource.verifyEmail(email, _code.value)
                val qrToken = response.qrToken
                if (qrToken.isNullOrBlank()) {
                    _errorResId.value = R.string.error_unexpected
                } else {
                    _effect.send(
                        VerifyUiEffect.NavigateToQr(
                            name = response.name.ifBlank { name },
                            email = response.email.ifBlank { email },
                            registrationId = response.id.ifBlank { registrationId },
                            qrToken = qrToken,
                        )
                    )
                }
            } catch (e: DataException) {
                _errorResId.value = when (e) {
                    is DataException.InvalidCode -> R.string.error_invalid_code
                    is DataException.CodeExpired -> {
                        _code.value = ""
                        R.string.error_code_expired
                    }
                    is DataException.RegistrationNotFound -> R.string.error_registration_not_found
                    is DataException.AlreadyVerified -> R.string.error_already_verified
                    is DataException.InvalidInput -> R.string.error_invalid_input
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

    fun resendCode() {
        if (_resendSecondsLeft.intValue > 0 || _isLoading.value || _isResending.value) return

        viewModelScope.launch {
            _isResending.value = true
            _errorResId.value = null
            _infoResId.value = null
            try {
                eventDatasource.resendVerificationCode(email)
                _code.value = ""
                _infoResId.value = R.string.verify_code_resent
                startResendCooldown()
            } catch (e: DataException) {
                _errorResId.value = when (e) {
                    is DataException.RegistrationNotFound -> R.string.error_registration_not_found
                    is DataException.AlreadyVerified -> R.string.error_already_verified
                    is DataException.NetworkError -> R.string.error_network
                    else -> R.string.error_server_fallback
                }
            } catch (_: Exception) {
                _errorResId.value = R.string.error_unexpected
            } finally {
                _isResending.value = false
            }
        }
    }

    private fun startResendCooldown() {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            for (second in RESEND_COOLDOWN_SECONDS downTo 1) {
                _resendSecondsLeft.intValue = second
                delay(1_000)
            }
            _resendSecondsLeft.intValue = 0
        }
    }

    private companion object {
        const val CODE_LENGTH = 6
        const val RESEND_COOLDOWN_SECONDS = 30
    }
}
