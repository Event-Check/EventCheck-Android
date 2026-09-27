package com.eventcheck.presentation.qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eventcheck.R

@Composable
fun QrCodeScreen(
    name: String,
    email: String,
    registrationId: String,
    onDone: () -> Unit,
    viewModel: QrCodeViewModel = hiltViewModel(),
) {
    LaunchedEffect(name, email, registrationId) {
        viewModel.setupData(name, email, registrationId)
    }

    val qrCodeBitmap by viewModel.qrCodeBitmap
    val displayName by viewModel.name
    val displayEmail by viewModel.email
    val displayRegId by viewModel.registrationId

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        QRScreenContent(
            qrCodeBitmap = qrCodeBitmap,
            displayName = displayName,
            displayEmail = displayEmail,
            displayRegId = displayRegId,
            onDone = onDone,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
@Composable
private fun QRScreenContent(
    qrCodeBitmap: Bitmap?,
    displayName: String,
    displayEmail: String,
    displayRegId: String,
    onDone: () -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.qr_screen_title),
                color = colorResource(R.color.user_text),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )

            Text(
                text = stringResource(R.string.qr_screen_subtitle),
                color = colorResource(R.color.user_text),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
            )

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.user_button),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                val bitmap = qrCodeBitmap
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Generated QR Code",
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    CircularProgressIndicator(
                        color = colorResource(R.color.user_button),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white),
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = displayName,
                        color = colorResource(R.color.user_text),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
                        ),
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = displayEmail,
                        color = colorResource(R.color.user_text),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                    )

                    if (displayRegId.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.qr_registration_id, displayRegId),
                            color = Color.Gray,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                            ),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.user_button),
                contentColor = Color.White,
            ),
        ) {
            Text(
                text = stringResource(R.string.btn_done),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
private fun Preview(){
    QRScreenContent(
        qrCodeBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) ,
        displayName = "Hend",
        displayEmail = "h@gmail.com",
        displayRegId ="" ,
        onDone ={} ,
        modifier = Modifier

    )
}