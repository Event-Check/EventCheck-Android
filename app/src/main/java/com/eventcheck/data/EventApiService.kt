package com.eventcheck.data

import com.eventcheck.data.request.CheckInRequest
import com.eventcheck.data.request.RegisterRequest
import com.eventcheck.data.request.ResendCodeRequest
import com.eventcheck.data.request.VerifyEmailRequest
import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.data.response.RegistrationResponse
import com.eventcheck.data.response.ReportSummaryResponse
import com.eventcheck.data.response.StatsResponse
import com.eventcheck.data.response.VerifyEmailResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface EventApiService {

    // USER
    @POST("registrations")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegistrationResponse

    @POST("registrations/verify")
    suspend fun verifyEmail(
        @Body request: VerifyEmailRequest
    ): VerifyEmailResponse

    @POST("registrations/resend-verification")
    suspend fun resendVerificationCode(
        @Body request: ResendCodeRequest
    ): RegistrationResponse

    // SCANNER
    @POST("check-in")
    suspend fun checkIn(
        @Body request: CheckInRequest
    ): CheckInResponse

    @GET("admin/stats")
    suspend fun getStats(): StatsResponse

    @GET("admin/report/summary")
    suspend fun getReportSummary(): ReportSummaryResponse

    @Streaming
    @GET("admin/report/export")
    suspend fun exportReport(
        @Query("format") format: String,
        @Query("includeAttendees") includeAttendees: Boolean = true
    ): ResponseBody
}
