package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.data.api.service.WeatherService
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeatherData(lat: Double, lon: Double): WeatherData {
        return weatherService.getCurrentWeather(lat = lat, lon = lon)
    }

    override suspend fun getForecastData(lat: Double, lon: Double): ForecastData {
        return weatherService.getForecastData(lat, lon)
    }
}