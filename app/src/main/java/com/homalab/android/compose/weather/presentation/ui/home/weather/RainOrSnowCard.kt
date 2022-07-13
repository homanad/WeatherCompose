package com.homalab.android.compose.weather.presentation.ui.home.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.domain.entity.subEntity.RainOrSnow
import com.homalab.android.compose.weather.presentation.components.DefaultVerticalSpacer
import com.homalab.android.compose.weather.util.Dimension4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RainOrSnowCard(title: String, rainOrSnow: RainOrSnow?, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimension4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val last1Hour = rainOrSnow?.`1h` ?: stringResource(id = R.string.no_data)
            val last3Hour = rainOrSnow?.`3h` ?: stringResource(id = R.string.no_data)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            DefaultVerticalSpacer()
            Text(
                text = stringResource(id = R.string.last_one_hour_pattern).format(last1Hour),
                style = MaterialTheme.typography.bodyMedium
            )
            DefaultVerticalSpacer()
            Text(
                text = stringResource(id = R.string.last_three_hours_pattern).format(last3Hour),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}