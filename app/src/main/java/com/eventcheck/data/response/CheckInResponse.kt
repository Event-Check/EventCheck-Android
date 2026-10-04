package com.eventcheck.data.response

import com.google.gson.annotations.SerializedName

data class CheckInResponse(
    @SerializedName("checkedIn")
    val checkedIn: Boolean,
    @SerializedName("checkedInAt")
    val checkedInAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("registrationId")
    val registrationId: String?,
    @SerializedName("status")
    val status: String
)