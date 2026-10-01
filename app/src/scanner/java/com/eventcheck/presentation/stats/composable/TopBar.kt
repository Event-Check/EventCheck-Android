package com.eventcheck.presentation.stats.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eventcheck.R

@Composable
fun TopBar(
    onBack: () -> Unit,
    title : String,
    subtitle : String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.back_button),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onBack)
                .background(Color.Transparent),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(
                text = title,
                color = colorResource(R.color.black),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_bold))
                )
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview(){
    TopBar(
        title = stringResource(R.string.title_stats_screen),
        subtitle = stringResource(R.string.subtitle_stats_screen),
        onBack = {}
    )
}