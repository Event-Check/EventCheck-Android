package com.eventcheck.presentation.form

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventcheck.R
import com.eventcheck.data.EventDatasource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException
import javax.inject.Inject


sealed interface FormUiEffect {
    data class NavigateToQr(
        val name: String,
        val email: String,
        val registrationId: String,
    ) : FormUiEffect
}

@HiltViewModel
class FormViewModel @Inject constructor(
    private val eventDatasource: EventDatasource
) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _nameError = mutableStateOf<String?>(null)
    val nameError: State<String?> = _nameError

    private val _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _generalError = mutableStateOf<Int?>(null)
    val generalError: State<Int?> = _generalError

    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess

    private val _effect = Channel<FormUiEffect>(Channel.BUFFERED)
    val effect: Flow<FormUiEffect> = _effect.receiveAsFlow()

    val isFormValid = derivedStateOf {
        _name.value.isNotBlank() && 
        _email.value.isNotBlank() && 
        _nameError.value == null && 
        _emailError.value == null
    }

    fun onNameChange(newName: String) {
        _name.value = newName
        validateName(newName)
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        validateEmail(newEmail)
    }

    private fun validateName(name: String) {
        _nameError.value = if (name.isBlank()) {
            "Name cannot be empty"
        } else if (name.length < 3) {
            "Name must be at least 3 characters"
        } else if (name.all { it.isDigit() || it.isWhitespace() }) {
            "Name cannot be only numbers"
        } else if (!name.all { it.isLetter() || it.isWhitespace() }) {
            "Name cannot contain special characters"
        } else {
            null
        }
    }

    private fun validateEmail(email: String) {
        _emailError.value = if (email.isBlank()) {
            "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email format"
        } else {
            null
        }
    }

    fun clearGeneralError() {
        _generalError.value = null
    }
    fun resetSuccessState() {
        _isSuccess.value = false
    }
    fun submitForm() {
        validateName(_name.value)
        validateEmail(_email.value)

        if (isFormValid.value) {
            viewModelScope.launch {
                _isLoading.value = true
                _generalError.value = null
                _isSuccess.value = false
                try {
//                    val response = eventDatasource.register(_name.value.trim(), _email.value.trim())
//                    val regName = response.name.ifBlank { _name.value.trim() }
//                    val regEmail = response.email.ifBlank { _email.value.trim() }

                    val regName = _name.value.trim()
                    val regEmail =  _email.value.trim()

                    _isSuccess.value = true
                    _effect.send(
                        FormUiEffect.NavigateToQr(
                            name = regName,
                            email = regEmail,
                            registrationId = "", //TODO add registration id
                        )
                    )
                } catch (e: HttpException) {
                    _generalError.value = when (e.code()) {
                        400 -> R.string.error_invalid_input
                        409 -> R.string.error_email_registered
                        else -> R.string.error_server_fallback
                    }
                } catch (_: IOException) {
                    _generalError.value = R.string.error_network
                } catch (_: Exception) {
                    _generalError.value = R.string.error_unexpected
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}
