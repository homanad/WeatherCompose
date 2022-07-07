package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.runtime.Composable
import com.homalab.android.compose.weather.presentation.components.MessageText

@Composable
fun DetailScreen(lat: Double, lon: Double) {

    MessageText(text = "DETAIL \n lat: $lat & lon: $lon")
}