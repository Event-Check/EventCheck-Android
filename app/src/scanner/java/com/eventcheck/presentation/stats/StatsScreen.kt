package com.eventcheck.presentation.stats

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.eventcheck.presentation.stats.composable.ExportCard
import com.eventcheck.presentation.stats.composable.ExportSuccessDialog
import com.eventcheck.presentation.stats.composable.StatsGrid
import com.eventcheck.presentation.stats.composable.TopBar

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading
    val isExporting by viewModel.isExporting
    val stats by viewModel.stats
    val includeAttendees by viewModel.includeAttendees
    val exportedFile by viewModel.exportedFile
    val errorResId by viewModel.errorResId

    LaunchedEffect(Unit) {
        viewModel.fetchStats()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        StatsScreenContent(
            context = context,
            onBack = onBack,
            stats = stats,
            includeAttendees = includeAttendees,
            onToggleIncludeAttendees = viewModel::toggleIncludeAttendees,
            onExportPdf = { viewModel.exportReport("pdf") },
            onExportExcel = { viewModel.exportReport("excel") },
            onFetchStatsClick = viewModel::fetchStats,
            clearError = viewModel::clearError,
            onExportHandled = viewModel::onExportHandled,
            isLoading = isLoading || isExporting,
            exportedFile = exportedFile,
            errorResId = errorResId,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(innerPadding)
        )
    }
}

@Composable
private fun StatsScreenContent(
    context: Context,
    onBack: () -> Unit,
    stats: StatsResponse?,
    includeAttendees: Boolean,
    onToggleIncludeAttendees: (Boolean) -> Unit,
    onExportPdf: () -> Unit,
    onExportExcel: () -> Unit,
    onFetchStatsClick: () -> Unit,
    clearError: () -> Unit,
    onExportHandled: () -> Unit,
    isLoading: Boolean,
    exportedFile: ExportedFile?,
    errorResId: Int?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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

                        Spacer(modifier = Modifier.height(20.dp))

                        ExportCard(
                            includeAttendees = includeAttendees,
                            onToggleIncludeAttendees = onToggleIncludeAttendees,
                            onExportPdf = onExportPdf,
                            onExportExcel = onExportExcel
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

        exportedFile?.let { file ->
            ExportSuccessDialog(
                exportedFile = file,
                onOpenFile = {
                    openExportedFile(context, file)
                    onExportHandled()
                },
                onDismiss = onExportHandled
            )
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

private fun openExportedFile(
    context: Context,
    exportedFile: ExportedFile
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                exportedFile.uri,
                exportedFile.mimeType
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(
                intent,
                "Open ${exportedFile.name}"
            )
        )
    } catch (_: Exception) {
        Toast.makeText(
            context,
            R.string.msg_no_app_to_open,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview
@Composable
private fun Preview() {
    StatsScreenContent(
        context = LocalContext.current,
        onBack = {},
        stats = StatsResponse(
            totalRegistrations = 2,
            verifiedRegistrations = 0,
            checkedIn = 0,
            notCheckedIn = 0
        ),
        includeAttendees = true,
        onToggleIncludeAttendees = {},
        onExportPdf = {},
        onExportExcel = {},
        onFetchStatsClick = {},
        clearError = {},
        onExportHandled = {},
        isLoading = false,
        exportedFile = null,
        errorResId = null
    )
}
