package com.eventcheck.presentation.stats.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eventcheck.R

@Composable
fun StatsGrid(
    totalRegistrations: String,
    verifiedRegistrations: String,
    checkedIn: String,
    notCheckedIn: String
){
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = stringResource(R.string.stat_total_registrations),
            value = totalRegistrations,
            modifier = Modifier.weight(1f),
            badgeColor = colorResource(R.color.badge_color)
        )

        StatCard(
            title = stringResource(R.string.stat_verified_registrations),
            value = verifiedRegistrations,
            modifier = Modifier.weight(1f),
            badgeColor = colorResource(R.color.verified_color)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = stringResource(R.string.stat_checked_in),
            value = checkedIn,
            modifier = Modifier.weight(1f),
            badgeColor = colorResource(R.color.scanner_button)
        )

        StatCard(
            title = stringResource(R.string.stat_not_checked_in),
            value = notCheckedIn,
            modifier = Modifier.weight(1f),
            badgeColor = colorResource(R.color.check_in)
        )
    }
}

@Preview
@Composable
private fun Preview(){
    StatsGrid(
        totalRegistrations = "100",
        verifiedRegistrations = "80",
        checkedIn = "50",
        notCheckedIn = "50"
    )
}