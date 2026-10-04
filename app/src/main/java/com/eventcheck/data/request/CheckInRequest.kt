package com.eventcheck.data.request

import com.google.gson.annotations.SerializedName

data class CheckInRequest(
    @SerializedName("qrToken")
    val qrToken: String
)
