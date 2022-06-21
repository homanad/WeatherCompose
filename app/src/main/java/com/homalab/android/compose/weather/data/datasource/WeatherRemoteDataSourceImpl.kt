package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.data.api.service.WeatherService
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherDataSource {

    override suspend fun getCurrentWeatherData(id: Int, lat: Float, lon: Float): WeatherData {
        return weatherService.getCurrentWeather(lat = lat, lon = lon)
    }
}