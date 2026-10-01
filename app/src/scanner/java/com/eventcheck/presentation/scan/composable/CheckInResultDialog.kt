package com.eventcheck.presentation.scan.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eventcheck.R
import com.eventcheck.data.response.CheckInResponse

@Composable
fun CheckInResultDialog(
    result: CheckInResponse,
    onScanNext: () -> Unit
) {
    val isSuccess = result.checkedIn || result.status.equals("SUCCESS", ignoreCase = true)

    AlertDialog(
        onDismissRequest = onScanNext,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSuccess) colorResource(R.color.scanner_button)
                            else MaterialTheme.colorScheme.error
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isSuccess) "✓" else "✕",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (isSuccess) stringResource(R.string.check_in_success)
                    else stringResource(R.string.check_in_failed),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_bold))
                    )
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = result.message,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )

                result.name?.let { name ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.label_dialog_name, name),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                result.email?.let { email ->
                    Text(
                        text = stringResource(R.string.label_dialog_email, email),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                result.registrationId?.let { regId ->
                    Text(
                        text = stringResource(R.string.label_dialog_id, regId),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onScanNext,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.scanner_button)
                )
            ) {
                Text(stringResource(R.string.btn_scan_next))
            }
        }
    )
}

@Preview
@Composable
private fun Preview(){
    CheckInResultDialog(
        result = CheckInResponse(
            checkedIn = true,
            message = "Check-in successful",
            checkedInAt = "",
            email = "h@gmail.com",
            name = "John Doe",
            registrationId = "123456",
            status = "SUCCESS"
        )
    ) { }
}