package com.eventcheck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.eventcheck.presentation.home.HomeScreen
import com.eventcheck.presentation.scan.ScanScreen
import com.eventcheck.presentation.stats.StatsScreen

@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<Any>(HomeKey) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<HomeKey> {
                HomeScreen(
                    onNavigateToScan = { backStack.add(ScanKey) },
                    onNavigateToStats = { backStack.add(StatsKey) },
                )
            }
            entry<ScanKey> {
                ScanScreen(
                    onBack = { backStack.removeLastOrNull() },
                )
            }
            entry<StatsKey> {
                StatsScreen(
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}