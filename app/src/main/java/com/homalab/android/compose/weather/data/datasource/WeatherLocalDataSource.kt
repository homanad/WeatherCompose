package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData

interface WeatherLocalDataSource {
    suspend fun getSavedWeatherData(id: Int): WeatherData
    suspend fun saveWeatherData(weatherData: WeatherData): Long
    suspend fun getLastWeatherData(): WeatherData
    suspend fun getSavedCities(): List<City>
    suspend fun getForecastData(lat: Double, lon: Double): ForecastData
    suspend fun saveForecastData(forecastData: ForecastData): Long
}