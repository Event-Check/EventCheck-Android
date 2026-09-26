package com.eventcheck.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailResponse(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("message")
    val message: String,
    @SerialName("emailVerified")
    val isEmailVerified: Boolean,
    @SerialName("id")
    val id: String,
    @SerialName("qrToken")
    val qrToken: String?,
)