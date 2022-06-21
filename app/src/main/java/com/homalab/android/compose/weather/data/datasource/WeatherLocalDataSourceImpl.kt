package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.data.db.WeatherDatabase
import com.homalab.android.compose.weather.data.mapper.toWeatherData
import com.homalab.android.compose.weather.data.mapper.toWeatherDataEntity
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDatabase: WeatherDatabase
) : WeatherDataSource {

    override suspend fun getCurrentWeatherData(id: Int, lat: Float, lon: Float): WeatherData {
        return weatherDatabase.weatherDataDao.getWeatherDataById(id).toWeatherData()
    }

    fun saveWeatherData(weatherData: WeatherData): Long {
        return weatherDatabase.weatherDataDao.insert(weatherData.toWeatherDataEntity())
    }
}