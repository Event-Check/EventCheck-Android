package com.eventcheck.data.response

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("emailVerified")
    val isEmailVerified: Boolean,
    @SerializedName("id")
    val id: String,
)