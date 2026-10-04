package com.eventcheck.data.request

import com.google.gson.annotations.SerializedName

data class ResendCodeRequest(
    @SerializedName("email")
    val email: String
)
