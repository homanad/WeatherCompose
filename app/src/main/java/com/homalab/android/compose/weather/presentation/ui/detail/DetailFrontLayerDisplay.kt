package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem

@Composable
fun DetailFrontLayerDisplay(
    forecastDayItem: ForecastDayItem?
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.fillMaxSize(), text = forecastDayItem.toString())

    }
}