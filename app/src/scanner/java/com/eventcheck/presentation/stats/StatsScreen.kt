package com.eventcheck.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eventcheck.R
import com.eventcheck.data.response.StatsResponse
import com.eventcheck.presentation.stats.composable.AttendanceProgressCard
import com.eventcheck.presentation.stats.composable.EmptyStatsState
import com.eventcheck.presentation.stats.composable.StatsGrid
import com.eventcheck.presentation.stats.composable.TopBar

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading
    val stats by viewModel.stats
    val errorResId by viewModel.errorResId

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        StatsScreenContent(
            onBack = onBack,
            stats = stats,
            onFetchStatsClick = viewModel::fetchStats,
            clearError = viewModel::clearError,
            isLoading = isLoading,
            errorResId = errorResId,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun StatsScreenContent(
    onBack: () -> Unit,
    stats: StatsResponse?,
    onFetchStatsClick: () -> Unit,
    clearError: () -> Unit,
    isLoading: Boolean,
    errorResId: Int?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopBar(
                title = stringResource(R.string.title_stats_screen),
                subtitle = stringResource(R.string.subtitle_stats_screen),
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.scanner_button)
                    )
                } else if (stats != null && stats.totalRegistrations > 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val attendanceRate = (stats.checkedIn.toFloat() / stats.totalRegistrations.toFloat())

                        AttendanceProgressCard(attendanceRate)
                        Spacer(modifier = Modifier.height(16.dp))
                        StatsGrid(
                            totalRegistrations = stats.totalRegistrations.toString(),
                            verifiedRegistrations = stats.verifiedRegistrations.toString(),
                            checkedIn = stats.checkedIn.toString(),
                            notCheckedIn = (stats.totalRegistrations - stats.checkedIn).toString()
                        )
                    }
                } else {
                    EmptyStatsState(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onFetchStatsClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.scanner_button),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_refresh),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (errorResId != null && stats != null && stats.totalRegistrations > 0) {
            AlertDialog(
                onDismissRequest = clearError,
                title = { Text(text = stringResource(R.string.error_title)) },
                text = { Text(text = stringResource(errorResId)) },
                confirmButton = {
                    TextButton(onClick = clearError) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    StatsScreenContent(
        onBack = {},
        stats = StatsResponse(
            totalRegistrations = 2,
            verifiedRegistrations = 0,
            checkedIn = 0,
            notCheckedIn = 0
        ),
        clearError = {},
        isLoading = false,
        errorResId = null,
        onFetchStatsClick = {}
    )
}
