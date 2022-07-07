package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeatherData(lat: Double, lon: Double): WeatherData
    suspend fun getForecastData(lat: Double, lon: Double): ForecastData
}