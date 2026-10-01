package com.eventcheck.data

import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.data.response.RegistrationResponse
import com.eventcheck.data.response.StatsResponse
import com.eventcheck.data.response.VerifyEmailResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class DataException(override val message: String? = null) : Exception(message) {
    class InvalidInput : DataException("Invalid input parameters")
    class CheckInFailed : DataException("Check-in failed or ticket not found")
    class EmailAlreadyRegistered : DataException("Email already registered")
    class NetworkError : DataException("Network connectivity issue")
    class ServerError : DataException("Server error occurred")
    class UnexpectedError : DataException("An unexpected error occurred")
}

class EventDatasource @Inject constructor(
    private val apiService: EventApiService
) {
    suspend fun register(name: String, email: String): RegistrationResponse {
        return handleApiCall { apiService.register(name, email) }
    }

    suspend fun verifyEmail(email: String, code: String): VerifyEmailResponse {
        return handleApiCall { apiService.verifyEmail(email, code) }
    }

    suspend fun resendVerificationCode(email: String): RegistrationResponse {
        return handleApiCall { apiService.resendVerificationCode(email) }
    }

    suspend fun checkIn(qrToken: String): CheckInResponse {
        return handleApiCall { apiService.checkIn(qrToken) }
    }

    suspend fun getStats(): StatsResponse {
        return handleApiCall { apiService.getStats() }
    }

    private inline fun <T> handleApiCall(call: () -> T): T {
        return try {
            call()
        } catch (e: HttpException) {
            throw when (e.code()) {
                400 -> DataException.InvalidInput()
                404 -> DataException.CheckInFailed()
                409 -> DataException.EmailAlreadyRegistered()
                in 500..599 -> DataException.ServerError()
                else -> DataException.ServerError()
            }
        } catch (_: IOException) {
            throw DataException.NetworkError()
        } catch (e: DataException) {
            throw e
        } catch (_: Exception) {
            throw DataException.UnexpectedError()
        }
    }
}