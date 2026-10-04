package com.eventcheck.presentation.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eventcheck.R

@Composable
fun VerifyScreen(
    name: String,
    email: String,
    registrationId: String,
    onNavigateToQr: (name: String, email: String, registrationId: String, qrToken: String) -> Unit,
    viewModel: VerifyViewModel = hiltViewModel(),
) {
    LaunchedEffect(name, email, registrationId) {
        viewModel.setup(name, email, registrationId)
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is VerifyUiEffect.NavigateToQr -> onNavigateToQr(
                    effect.name,
                    effect.email,
                    effect.registrationId,
                    effect.qrToken,
                )
            }
        }
    }

    val code by viewModel.code
    val isCodeComplete by viewModel.isCodeComplete
    val isLoading by viewModel.isLoading
    val isResending by viewModel.isResending
    val errorResId by viewModel.errorResId
    val infoResId by viewModel.infoResId
    val resendSecondsLeft by viewModel.resendSecondsLeft

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        VerifyScreenContent(
            modifier = Modifier.padding(innerPadding),
            email = email,
            code = code,
            isLoading = isLoading,
            isResending = isResending,
            isBusy = isLoading || isResending,
            isVerifyEnabled = isCodeComplete && !isLoading && !isResending,
            errorResId = errorResId,
            infoResId = infoResId,
            resendSecondsLeft = resendSecondsLeft,
            onCodeChange = viewModel::onCodeChange,
            onVerify = viewModel::verify,
            onResend = viewModel::resendCode,
        )
    }
}

@Composable
private fun VerifyScreenContent(
    email: String,
    code: String,
    isLoading: Boolean,
    isResending: Boolean,
    isBusy: Boolean,
    isVerifyEnabled: Boolean,
    errorResId: Int?,
    infoResId: Int?,
    resendSecondsLeft: Int,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp, horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.verify_title),
            color = colorResource(R.color.user_text),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_bold)),
            ),
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Text(
            text = stringResource(R.string.verify_subtitle, email),
            color = colorResource(R.color.user_text),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
            modifier = Modifier.padding(bottom = 48.dp),
        )

        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            enabled = !isBusy,
            singleLine = true,
            isError = errorResId != null,
            textStyle = TextStyle(
                color = colorResource(R.color.user_text),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 8.sp,
                textAlign = TextAlign.Center,
            ),
            placeholder = {
                Text(
                    text = stringResource(R.string.verify_code_placeholder),
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 28.sp, letterSpacing = 8.sp),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { onVerify() }),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        errorResId?.let {
            Text(
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            )
        }

        infoResId?.let {
            Text(
                text = stringResource(it),
                color = colorResource(R.color.user_button),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            )
        }

        Button(
            onClick = onVerify,
            enabled = isVerifyEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.user_button),
                contentColor = Color.White,
                disabledContainerColor = colorResource(R.color.user_button_disabled),
                disabledContentColor = Color.White,
            ),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.btn_verify),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.verify_didnt_receive),
                color = colorResource(R.color.user_text),
                style = TextStyle(fontSize = 14.sp),
            )
            TextButton(
                onClick = onResend,
                enabled = resendSecondsLeft == 0 && !isBusy,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = colorResource(R.color.user_button),
                    disabledContentColor = colorResource(R.color.user_button_disabled),
                ),
            ) {
                if (isResending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = colorResource(R.color.user_button),
                        strokeWidth = 1.5.dp,
                    )
                } else {
                    Text(
                        text = if (resendSecondsLeft > 0) {
                            stringResource(R.string.resend_code_in, resendSecondsLeft)
                        } else {
                            stringResource(R.string.btn_resend_code)
                        },
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VerifyScreenContent(
        email = "hend@test.com",
        code = "123",
        isLoading = false,
        isResending = false,
        isBusy = false,
        isVerifyEnabled = false,
        errorResId = null,
        infoResId = null,
        resendSecondsLeft = 42,
        onCodeChange = {},
        onVerify = {},
        onResend = {},
    )
}