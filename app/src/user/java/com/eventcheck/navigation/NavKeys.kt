package com.eventcheck.navigation

data object FormKey : NavKey

data class QrKey(
    val name: String,
    val email: String,
    val registrationId: String,
) : NavKey
