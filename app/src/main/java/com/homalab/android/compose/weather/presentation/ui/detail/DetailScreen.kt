package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.presentation.components.LargeSpacer
import com.homalab.android.compose.weather.presentation.components.MessageText
import com.homalab.android.compose.weather.presentation.mapper.toForecastDayData
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel

@Composable
fun DetailScreen(
    lat: Double,
    lon: Double,
    detailState: DetailState,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    Column {

        MessageText(text = "DETAIL \n lat: $lat & lon: $lon")

        LargeSpacer()

//        MessageText(text = "Forecast data: ${detailState.forecastData}")

        DetailDisplay(forecastDayData = detailState.forecastData?.toForecastDayData())
    }

}

@Stable
class DetailState(
    forecastData: ForecastData?
) {
    val forecastData by mutableStateOf(forecastData)
}

@Composable
fun rememberDetailState(
    forecastData: ForecastData? = null
) = remember {
    DetailState(forecastData)
}