package com.homalab.android.compose.weather.data.datasource

import com.homalab.android.compose.weather.data.db.dao.ForecastDataDao
import com.homalab.android.compose.weather.data.db.dao.WeatherDataDao
import com.homalab.android.compose.weather.data.mapper.*
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.ForecastData
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDataDao: WeatherDataDao,
    private val forecastDataDao: ForecastDataDao
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

    override suspend fun getSavedCities(): List<City> {
        return weatherDataDao.getWeathers().map { it.toCity() }
    }

    override suspend fun getForecastData(lat: Double, lon: Double): ForecastData {
        return forecastDataDao.getForecastData(lat, lon).toForecastData()
    }

    override suspend fun saveForecastData(forecastData: ForecastData): Long {
        return forecastDataDao.insert(forecastData.toForecastDataEntity())
    }
}