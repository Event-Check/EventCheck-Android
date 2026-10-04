package com.eventcheck.presentation.scan.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eventcheck.R
import com.eventcheck.data.response.CheckInResponse

@Composable
fun CheckInResultDialog(
    result: CheckInResponse,
    onScanNext: () -> Unit
) {
    val isSuccess = result.checkedIn || result.status.equals("SUCCESS", ignoreCase = true)

    val themeColor = if (isSuccess) colorResource(R.color.scanner_button) else Color(0xFFD32F2F)
    val headerBgColor = if (isSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

    Dialog(
        onDismissRequest = onScanNext,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Status Icon Badge
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(headerBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(themeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSuccess) "✓" else "✕",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = if (isSuccess) stringResource(R.string.check_in_success)
                    else stringResource(R.string.check_in_failed),
                    color = themeColor,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_bold))
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Status Message
                Text(
                    text = result.message,
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                // Attendee Details Card
                if (result.name != null || result.email != null || result.registrationId != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9F8))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            result.name?.let { name ->
                                Text(
                                    text = stringResource(R.string.label_dialog_name, name),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            result.email?.let { email ->
                                Text(
                                    text = stringResource(R.string.label_dialog_email, email),
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }

                            result.registrationId?.let { regId ->
                                Text(
                                    text = stringResource(R.string.label_dialog_id, regId),
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Prominent Action Button
                Button(
                    onClick = onScanNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.btn_scan_next),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSuccess(){
    CheckInResultDialog(
        result = CheckInResponse(
            checkedIn = true,
            message = "Check-in successful",
            checkedInAt = "",
            email = "john@gmail.com",
            name = "John Doe",
            registrationId = "123456",
            status = "SUCCESS"
        )
    ) { }
}

@Preview
@Composable
private fun PreviewError(){
    CheckInResultDialog(
        result = CheckInResponse(
            checkedIn = false,
            message = "Ticket already checked in",
            checkedInAt = "",
            email = "john@gmail.com",
            name = "John Doe",
            registrationId = "123456",
            status = "FAILED"
        )
    ) { }
}
