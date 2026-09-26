package com.eventcheck.data

import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.data.response.RegistrationResponse
import com.eventcheck.data.response.StatsResponse
import com.eventcheck.data.response.VerifyEmailResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface EventApiService {

    //USER
    @POST("registrations")
    suspend fun register(name: String, email: String): RegistrationResponse

    @POST("registrations/verify")
    suspend fun verifyEmail(email: String, code: String): VerifyEmailResponse

    @POST("registrations/resend-verification")
    suspend fun resendVerificationCode(email: String): RegistrationResponse

    //SCANNER
    @POST("check-in")
    suspend fun checkIn(qrToken: String): CheckInResponse

    @GET("admin/stats")
    suspend fun getStats(): StatsResponse

}