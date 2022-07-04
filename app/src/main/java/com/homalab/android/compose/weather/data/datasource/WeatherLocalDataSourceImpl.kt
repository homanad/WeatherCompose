package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.data.db.dao.WeatherDataDao
import com.homalab.android.compose.weather.data.mapper.toWeatherData
import com.homalab.android.compose.weather.data.mapper.toWeatherDataEntity
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDataDao: WeatherDataDao
) : WeatherDataSource {

    override suspend fun getCurrentWeatherData(id: Int, lat: Double, lon: Double): WeatherData {
        return weatherDataDao.getWeatherDataById(id).toWeatherData()
    }

    fun saveWeatherData(weatherData: WeatherData): Long {
        return weatherDataDao.removeLastIfNeededAndSaveNew(weatherData.toWeatherDataEntity())
    }

    fun getLastWeatherData(): WeatherData {
        return weatherDataDao.getLastWeatherData().toWeatherData()
    }
}