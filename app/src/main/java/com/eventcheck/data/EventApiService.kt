package com.eventcheck.data

import com.eventcheck.data.request.CheckInRequest
import com.eventcheck.data.request.RegisterRequest
import com.eventcheck.data.request.ResendCodeRequest
import com.eventcheck.data.request.VerifyEmailRequest
import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.data.response.RegistrationResponse
import com.eventcheck.data.response.StatsResponse
import com.eventcheck.data.response.VerifyEmailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}
