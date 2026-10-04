package com.eventcheck.data

import com.eventcheck.data.request.CheckInRequest
import com.eventcheck.data.request.RegisterRequest
import com.eventcheck.data.request.ResendCodeRequest
import com.eventcheck.data.request.VerifyEmailRequest
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
    suspend fun register(request: RegisterRequest): RegistrationResponse {
        return handleApiCall { apiService.register(request) }
    }

    suspend fun register(name: String, email: String): RegistrationResponse {
        return register(RegisterRequest(name = name, email = email))
    }

    suspend fun verifyEmail(request: VerifyEmailRequest): VerifyEmailResponse {
        return handleApiCall { apiService.verifyEmail(request) }
    }

    suspend fun verifyEmail(email: String, code: String): VerifyEmailResponse {
        return verifyEmail(VerifyEmailRequest(email = email, code = code))
    }

    suspend fun resendVerificationCode(request: ResendCodeRequest): RegistrationResponse {
        return handleApiCall { apiService.resendVerificationCode(request) }
    }

    suspend fun resendVerificationCode(email: String): RegistrationResponse {
        return resendVerificationCode(ResendCodeRequest(email = email))
    }

    suspend fun checkIn(request: CheckInRequest): CheckInResponse {
        return handleApiCall { apiService.checkIn(request) }
    }

    suspend fun checkIn(qrToken: String): CheckInResponse {
        return checkIn(CheckInRequest(qrToken = qrToken))
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
