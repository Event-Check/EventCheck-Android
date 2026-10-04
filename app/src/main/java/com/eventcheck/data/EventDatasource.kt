package com.eventcheck.data

import com.eventcheck.data.request.CheckInRequest
import com.eventcheck.data.request.RegisterRequest
import com.eventcheck.data.request.ResendCodeRequest
import com.eventcheck.data.request.VerifyEmailRequest
import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.data.response.RegistrationResponse
import com.eventcheck.data.response.StatsResponse
import com.eventcheck.data.response.VerifyEmailResponse
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

sealed class DataException(override val message: String? = null) : Exception(message) {
    class InvalidInput : DataException("Invalid input parameters")
    class CheckInFailed : DataException("Check-in failed or ticket not found")
    class EmailAlreadyRegistered : DataException("Email already registered")
    class InvalidCode : DataException("Invalid verification code")
    class CodeExpired : DataException("Verification code expired")
    class RegistrationNotFound : DataException("Registration not found")
    class AlreadyVerified : DataException("Email already verified")
    class NetworkError : DataException("Network connectivity issue")
    class ServerError : DataException("Server error occurred")
    class UnexpectedError : DataException("An unexpected error occurred")
}

class EventDatasource @Inject constructor(
    private val apiService: EventApiService
) {
    suspend fun register(name: String, email: String): RegistrationResponse {
        return handleApiCall(::mapDefaultError) {
            apiService.register(RegisterRequest(name, email))
        }
    }

    suspend fun verifyEmail(email: String, code: String): VerifyEmailResponse {
        return handleApiCall(::mapVerificationError) {
            apiService.verifyEmail(VerifyEmailRequest(email, code))
        }
    }

    suspend fun resendVerificationCode(email: String): RegistrationResponse {
        return handleApiCall(::mapVerificationError) {
            apiService.resendVerificationCode(ResendCodeRequest(email))
        }
    }

    suspend fun checkIn(qrToken: String): CheckInResponse {
        return handleApiCall(::mapDefaultError) {
            apiService.checkIn(CheckInRequest(qrToken))
        }
    }

    suspend fun getStats(): StatsResponse {
        return handleApiCall(::mapDefaultError) { apiService.getStats() }
    }

    private suspend fun <T> handleApiCall(
        mapHttpError: (HttpException) -> DataException,
        call: suspend () -> T,
    ): T {
        return try {
            call()
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            throw mapHttpError(e)
        } catch (_: IOException) {
            throw DataException.NetworkError()
        } catch (e: DataException) {
            throw e
        } catch (_: Exception) {
            throw DataException.UnexpectedError()
        }
    }

    private fun mapDefaultError(e: HttpException): DataException = when (e.code()) {
        400 -> DataException.InvalidInput()
        404 -> DataException.CheckInFailed()
        409 -> DataException.EmailAlreadyRegistered()
        else -> DataException.ServerError()
    }

    /** Error mapping used by verify email and resend code. */
    private fun mapVerificationError(e: HttpException): DataException {
        val message = e.serverMessage().orEmpty()
        return when (e.code()) {
            400 -> when {
                message.contains("expired", ignoreCase = true) ||
                        message.contains("No verification code", ignoreCase = true) ->
                    DataException.CodeExpired()

                message.contains("Invalid verification", ignoreCase = true) ->
                    DataException.InvalidCode()

                else -> DataException.InvalidInput()
            }

            404 -> DataException.RegistrationNotFound()
            409 -> DataException.AlreadyVerified()
            else -> DataException.ServerError()
        }
    }

    /** Reads the backend error body: {"message": "..."} */
    private fun HttpException.serverMessage(): String? = try {
        val body = response()?.errorBody()?.string()
        JsonParser.parseString(body).asJsonObject.get("message")?.asString
    } catch (_: Exception) {
        null
    }
}
