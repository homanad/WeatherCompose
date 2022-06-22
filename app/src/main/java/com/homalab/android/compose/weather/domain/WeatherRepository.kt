package com.homalab.android.compose.weather.domain

import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeatherData(id: Int, lat: Float, lon: Float): WeatherData
    suspend fun getLastWeatherData(): WeatherData
}