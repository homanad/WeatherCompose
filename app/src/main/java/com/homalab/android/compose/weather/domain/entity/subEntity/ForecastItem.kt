package com.homalab.android.compose.weather.domain.entity.subEntity

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<WeatherItem>,
    val wind: Wind,
    val visibility: Int,
    val pop: Float,
    val sys: ForecastSys,
    val dt_txt: String
)