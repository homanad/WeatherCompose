package com.homalab.android.compose.weather.data.repository

import com.homalab.android.compose.weather.data.datasource.WeatherLocalDataSourceImpl
import com.homalab.android.compose.weather.data.datasource.WeatherRemoteDataSourceImpl
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.domain.WeatherRepository
import com.homalab.android.compose.weather.domain.entity.WeatherData
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val weatherRemoteDataSource: WeatherRemoteDataSourceImpl,
    private val weatherLocalDataSource: WeatherLocalDataSourceImpl
) : WeatherRepository {

    override suspend fun getCurrentWeatherData(id: Int, lat: Float, lon: Float): WeatherData {
        return if (isNetworkAvailable()) {
            val data = weatherRemoteDataSource.getCurrentWeatherData(id, lat, lon)
            weatherLocalDataSource.saveWeatherData(data)
            return data
        } else {
            weatherLocalDataSource.getCurrentWeatherData(id, lat, lon)
        }
    }

    override suspend fun getLastWeatherData(): WeatherData {
        val lastData = weatherLocalDataSource.getLastWeatherData()
        return if (isNetworkAvailable()) {
            weatherRemoteDataSource.getCurrentWeatherData(
                lastData.id,
                lastData.coord.lat.toFloat(),
                lastData.coord.lon.toFloat()
            )
        } else lastData
    }

    private fun isNetworkAvailable() = networkChecker.getConnectionType() != NetworkChecker.NONE
}