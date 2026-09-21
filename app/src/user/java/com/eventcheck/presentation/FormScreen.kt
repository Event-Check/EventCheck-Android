package com.eventcheck.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun FormScreen(
    viewModel: FormViewModel = hiltViewModel()
) {
    val name by viewModel.name
    val email by viewModel.email
    val nameError by viewModel.nameError
    val emailError by viewModel.emailError
    val isFormValid by viewModel.isFormValid

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        FormScreenContent(
            modifier = Modifier.padding(innerPadding),
            name = name,
            email = email,
            nameError = nameError,
            emailError = emailError,
            isFormValid = isFormValid,
            onNameChange = viewModel::onNameChange,
            onEmailChange = viewModel::onEmailChange,
            onSubmit = viewModel::submitForm
        )
    }
}

@Composable
private fun FormScreenContent(
    name: String,
    email: String,
    nameError: String?,
    emailError: String?,
    isFormValid: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {

        Text(
            text = stringResource(R.string.enter_details),
            color = colorResource(R.color.user_text),
            textAlign = TextAlign.Left,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_bold))
            ),
            modifier = Modifier.padding(bottom = 64.dp)
        )

        Image(
            painter = painterResource(R.drawable.ic_launcher_playstore),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .size(120.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )

        CustomTextField(
            label = stringResource(R.string.label_name),
            placeholder = stringResource(R.string.placeholder_name),
            value = name,
            onValueChange = onNameChange,
            error = nameError
        )

        CustomTextField(
            label = stringResource(R.string.label_email),
            placeholder = stringResource(R.string.placeholder_email),
            value = email,
            onValueChange = onEmailChange,
            error = emailError,
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 128.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.user_button),
                contentColor = Color.White,
                disabledContainerColor = colorResource(R.color.user_button_disabled),
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.btn_submit),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CustomTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = colorResource(R.color.user_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
            isError = error != null,
            singleLine = true,
            label = {
                Text(
                    text = label,
                    color = colorResource(R.color.user_text),
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_bold))
                    )
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    textAlign = TextAlign.Left,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FormScreenContent(
        name = "",
        email = "",
        nameError = null,
        emailError = null,
        isFormValid = false,
        onNameChange = {},
        onEmailChange = {},
        onSubmit = {}
    )
}
