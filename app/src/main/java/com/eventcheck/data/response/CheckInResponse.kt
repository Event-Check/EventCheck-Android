package com.eventcheck.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckInResponse(
    @SerialName("checkedIn")
    val checkedIn: Boolean,
    @SerialName("checkedInAt")
    val checkedInAt: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("message")
    val message: String,
    @SerialName("name")
    val name: String?,
    @SerialName("registrationId")
    val registrationId: String?,
    @SerialName("status")
    val status: String
)