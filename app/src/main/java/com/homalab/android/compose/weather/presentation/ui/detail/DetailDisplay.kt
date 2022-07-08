package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.homalab.android.compose.weather.R
import com.homalab.android.compose.weather.presentation.components.HorizontalDivider
import com.homalab.android.compose.weather.presentation.components.MessageText
import com.homalab.android.compose.weather.presentation.components.SmallSpacer
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayData
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2

@Composable
fun DetailDisplay(
    forecastDayData: ForecastDayData?,
    modifier: Modifier = Modifier,
) {
    if (forecastDayData != null) {
        DetailInfo(forecastDayData = forecastDayData, modifier)
    } else {
        MessageText(
            text = stringResource(id = R.string.network_unavailable)
        )
    }
}

@Composable
fun DetailInfo(forecastDayData: ForecastDayData, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        DataChart(title = "Temp", data = forecastDayData.items[0].list.map { it.main.temp })
    }
}

@Composable
fun DataChart(
    title: String,
    data: List<Float>
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimension2, end = Dimension2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            SmallSpacer()
            HorizontalDivider(modifier = Modifier.padding(start = Dimension1))
        }
    }
}