package com.eventcheck.data

import javax.inject.Inject

class EventDatasource @Inject constructor(
    private val apiService: EventApiService
) {
    suspend fun register(name: String, email: String) =  apiService.register(name, email)

    suspend fun verifyEmail(email: String, code: String) = apiService.verifyEmail(email, code)

    suspend fun resendVerificationCode(email: String) =  apiService.resendVerificationCode(email)

    suspend fun checkIn(qrToken: String) = apiService.checkIn(qrToken)

    suspend fun getStats() = apiService.getStats()
}