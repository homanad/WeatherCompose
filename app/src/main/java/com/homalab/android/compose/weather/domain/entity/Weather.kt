package com.homalab.android.compose.weather.domain.entity

data class Weather(
    val weatherList: List<WeatherItem>
)

data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)