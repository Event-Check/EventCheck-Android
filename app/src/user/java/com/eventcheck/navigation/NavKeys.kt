package com.eventcheck.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object FormKey : NavKey

@Serializable
data class VerifyKey(
    val name: String,
    val email: String,
    val registrationId: String,
) : NavKey
@Serializable
data class QrKey(
    val name: String,
    val email: String,
    val registrationId: String,
    val qrToken: String,
) : NavKey
