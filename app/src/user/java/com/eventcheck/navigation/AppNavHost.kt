package com.eventcheck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavKey
import com.eventcheck.presentation.form.FormScreen
import com.eventcheck.presentation.qr.QrCodeScreen
import com.eventcheck.presentation.verify.VerifyScreen


@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<NavKey>(FormKey) }

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
                    onNavigateToVerify = { name, email, registrationId ->
                        backStack.add(VerifyKey(name, email, registrationId))
                    },
                )
            }
            entry<VerifyKey> { key ->
                VerifyScreen(
                    name = key.name,
                    email = key.email,
                    registrationId = key.registrationId,
                    onNavigateToQr = { name, email, registrationId, qrToken ->
                        backStack.removeLastOrNull()
                        backStack.add(QrKey(name, email, registrationId, qrToken))
                    },
                )
            }
            entry<QrKey> { key ->
                QrCodeScreen(
                    name = key.name,
                    email = key.email,
                    registrationId = key.registrationId,
                    qrToken = key.qrToken,
                    onDone = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}