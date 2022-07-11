package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.presentation.mapper.toForecastDayData
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel

@Composable
fun DetailScreen(
    lat: Double,
    lon: Double,
    detailState: DetailState,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(lat, lon) {
        detailState.forecastData = mainViewModel.getForecastData(lat, lon)
    }
    DetailDisplay(
        forecastDayData = detailState.forecastData?.toForecastDayData(),
        modifier = Modifier.fillMaxSize()
    )
}

@Stable
class DetailState(
    forecastData: ForecastData?
) {
    var forecastData by mutableStateOf(forecastData)
}

@Composable
fun rememberDetailState(
    forecastData: ForecastData? = null
) = remember {
    DetailState(forecastData)
}