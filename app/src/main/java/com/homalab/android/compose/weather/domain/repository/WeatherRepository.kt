package com.homalab.android.compose.weather.domain.repository

import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeatherData(id: Int, lat: Double, lon: Double): WeatherData
    suspend fun getLastWeatherData(): WeatherData
    suspend fun getSavedWeathers(): List<WeatherData>
}