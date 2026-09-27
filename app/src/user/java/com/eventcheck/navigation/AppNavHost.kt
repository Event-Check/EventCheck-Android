package com.eventcheck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.eventcheck.presentation.form.FormScreen
import com.eventcheck.presentation.qr.QrCodeScreen


@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<Any>(FormKey) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<FormKey> {
                FormScreen(
                    onNavigateToQr = { name, email, registrationId ->
                        backStack.add(QrKey(name, email, registrationId))
                    },
                )
            }
            entry<QrKey> { key ->
                QrCodeScreen(
                    name = key.name,
                    email = key.email,
                    registrationId = key.registrationId,
                    onDone = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}