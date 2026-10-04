package com.eventcheck.data.response

import com.google.gson.annotations.SerializedName

data class StatsResponse(
    @SerializedName("checkedIn")
    val checkedIn: Int,
    @SerializedName("notCheckedIn")
    val notCheckedIn: Int,
    @SerializedName("totalRegistrations")
    val totalRegistrations: Int,
    @SerializedName("verifiedRegistrations")
    val verifiedRegistrations: Int
)