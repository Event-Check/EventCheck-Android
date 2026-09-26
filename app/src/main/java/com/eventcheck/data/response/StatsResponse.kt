package com.eventcheck.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatsResponse(
    @SerialName("checkedIn")
    val checkedIn: Int,
    @SerialName("notCheckedIn")
    val notCheckedIn: Int,
    @SerialName("totalRegistrations")
    val totalRegistrations: Int,
    @SerialName("verifiedRegistrations")
    val verifiedRegistrations: Int
)