package com.eventcheck.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeKey : NavKey

@Serializable
data object ScanKey : NavKey

@Serializable
data object StatsKey : NavKey