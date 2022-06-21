package com.homalab.android.compose.weather.data.repository

import com.homalab.android.compose.weather.data.datasource.WeatherLocalDataSourceImpl
import com.homalab.android.compose.weather.data.datasource.WeatherRemoteDataSourceImpl
import com.homalab.android.compose.weather.domain.WeatherRepository
import com.homalab.android.compose.weather.domain.entity.WeatherData

class WeatherRepositoryImpl(
    private val weatherRemoteDataSource: WeatherRemoteDataSourceImpl,
    private val weatherLocalDataSource: WeatherLocalDataSourceImpl
) : WeatherRepository {

    override suspend fun getCurrentWeatherData(id: Int, lat: Float, lon: Float): WeatherData {
        return if (true) { //TODO internet available
            val data = weatherRemoteDataSource.getCurrentWeatherData(id, lat, lon)
            weatherLocalDataSource.saveWeatherData(data)
            return data
        } else {
            weatherLocalDataSource.getCurrentWeatherData(id, lat, lon)
        }
    }
}