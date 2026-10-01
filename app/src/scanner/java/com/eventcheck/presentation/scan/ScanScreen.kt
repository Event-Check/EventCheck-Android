package com.eventcheck.presentation.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.eventcheck.R
import com.eventcheck.data.response.CheckInResponse
import com.eventcheck.presentation.scan.composable.CameraPreviewView
import com.eventcheck.presentation.scan.composable.CheckInResultDialog
import com.eventcheck.presentation.stats.composable.TopBar

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScanScreen(
    onBack: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val isScanning by viewModel.isScanning
    val isLoading by viewModel.isLoading
    val checkInResult by viewModel.checkInResult
    val errorResId by viewModel.errorResId

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(innerPadding)
        ) {
            ScanScreenContent(
                onBack = onBack,
                hasCameraPermission = hasCameraPermission,
                isScanning = isScanning,
                isLoading = isLoading,
                checkInResult = checkInResult,
                launcher = launcher,
                onQrScanned = viewModel::onQrScanned
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(R.color.scanner_button)
                )
            }

            checkInResult?.let { result ->
                CheckInResultDialog(
                    result = result,
                    onScanNext = viewModel::resetScanState
                )
            }

            errorResId?.let { resId ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text(text = stringResource(R.string.error_title)) },
                    text = { Text(text = stringResource(resId)) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun ScanScreenContent(
    onBack: () -> Unit,
    hasCameraPermission: Boolean,
    isScanning: Boolean,
    isLoading: Boolean,
    checkInResult: CheckInResponse?,
    onQrScanned: (String) -> Unit,
    launcher: ActivityResultLauncher<String>
){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        TopBar(
            title = stringResource(R.string.title_scan_screen),
            subtitle = stringResource(R.string.subtitle_scan_screen),
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (hasCameraPermission) {
                    CameraPreviewView(
                        isScanning = isScanning && !isLoading && checkInResult == null,
                        onQrCodeDetected = onQrScanned
                    )

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .align(Alignment.Center)
                            .border(
                                width = 3.dp,
                                color = colorResource(R.color.scanner_button),
                                shape = RoundedCornerShape(20.dp)
                            )
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.camera_permission_required),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { launcher.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.scanner_button)
                            )
                        ) {
                            Text(stringResource(R.string.btn_grant_permission))
                        }
                    }
                }
            }
        }
    }
}