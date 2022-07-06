package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.data.db.dao.WeatherDataDao
import com.homalab.android.compose.weather.data.mapper.toWeatherData
import com.homalab.android.compose.weather.data.mapper.toWeatherDataEntity
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDataDao: WeatherDataDao
) : WeatherLocalDataSource {

    override suspend fun getSavedWeatherData(id: Int): WeatherData {
        return weatherDataDao.getWeatherDataById(id).toWeatherData()
    }

    override suspend fun saveWeatherData(weatherData: WeatherData): Long {
        return weatherDataDao.removeLastIfNeededAndSaveNew(weatherData.toWeatherDataEntity())
    }

    override suspend fun getLastWeatherData(): WeatherData {
        return weatherDataDao.getLastWeatherData().toWeatherData()
    }

    override suspend fun getSavedWeathers(): List<WeatherData> {
        return weatherDataDao.getWeathers().map { it.toWeatherData() }
    }
}