package com.homalab.android.compose.weather.domain.entity.subEntity

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
