package com.eventcheck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.eventcheck.presentation.home.HomeScreen
import com.eventcheck.presentation.scan.ScanQrScreen
import com.eventcheck.presentation.stats.StatsScreen

@Composable
fun ScanNavHost(){
    val backStack = remember { mutableStateListOf<NavKey>(HomeScreen) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<HomeScreen> {
                HomeScreen(
                    onScanClick = { backStack.add(ScanScreen) },
                    onShowStatsClick = { backStack.add(StatsScreen) }
                )
            }
            entry<ScanScreen> {
                ScanQrScreen()
            }
            entry<StatsScreen> {
                StatsScreen()
            }
        }
    )
}