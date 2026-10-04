package com.eventcheck.data.response

import com.google.gson.annotations.SerializedName

data class VerifyEmailResponse(
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
    @SerializedName("qrToken")
    val qrToken: String?,
)