package com.homalab.android.compose.weather.domain.entity.subEntity

data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)