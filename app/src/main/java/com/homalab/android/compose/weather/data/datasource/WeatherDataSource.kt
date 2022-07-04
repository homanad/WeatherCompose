package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherDataSource {
    suspend fun getCurrentWeatherData(id: Int, lat: Double, lon: Double): WeatherData
}