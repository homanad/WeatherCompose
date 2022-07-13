package com.homalab.android.compose.weather.domain.entity.subEntity

data class Main(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Float,
    val humidity: Float,
    val sea_level: Int
)