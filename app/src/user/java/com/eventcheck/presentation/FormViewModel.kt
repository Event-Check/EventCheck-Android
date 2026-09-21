package com.eventcheck.presentation

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor() : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _nameError = mutableStateOf<String?>(null)
    val nameError: State<String?> = _nameError

    private val _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

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

    fun submitForm() {
        validateName(_name.value)
        validateEmail(_email.value)

        if (isFormValid.value) {
            // Perform form submission logic here
        }
    }
}
