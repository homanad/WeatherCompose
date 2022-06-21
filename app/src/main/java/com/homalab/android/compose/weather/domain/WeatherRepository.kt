package com.homalab.android.compose.weather.domain

import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeatherData(lat: Float, lon: Float): WeatherData
}