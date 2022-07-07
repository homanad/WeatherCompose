package com.homalab.android.compose.weather.domain.repository

import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeatherData(id: Int, lat: Double, lon: Double): WeatherData
    suspend fun getLastWeatherData(): WeatherData
    suspend fun getSavedCities(): List<City>

    suspend fun getForecastData(): ForecastData
}