package com.eventcheck.data.response

import com.google.gson.annotations.SerializedName

data class ReportSummaryResponse(
    @SerializedName("registered")
    val registered: Int,

    @SerializedName("verified")
    val verified: Int,

    @SerializedName("checkedIn")
    val checkedIn: Int,

    @SerializedName("noShows")
    val noShows: Int,

    @SerializedName("attendanceRate")
    val attendanceRate: Double,
)
