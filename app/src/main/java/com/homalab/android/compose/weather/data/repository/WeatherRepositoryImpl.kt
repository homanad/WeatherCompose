package com.homalab.android.compose.weather.data.repository

import com.homalab.android.compose.weather.data.datasource.WeatherLocalDataSource
import com.homalab.android.compose.weather.data.datasource.WeatherRemoteDataSource
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
//    private val networkChecker: NetworkChecker,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) : WeatherRepository {

    override suspend fun getCurrentWeatherData(id: Int, lat: Double, lon: Double): WeatherData {
        return try {
            val data = weatherRemoteDataSource.getCurrentWeatherData(lat, lon)
            weatherLocalDataSource.saveWeatherData(data)
            return data
        } catch (e: Exception) {
            weatherLocalDataSource.getSavedWeatherData(id)
        }
    }

    override suspend fun getLastWeatherData(): WeatherData {
        val lastData = weatherLocalDataSource.getLastWeatherData()
        return try {
            weatherRemoteDataSource.getCurrentWeatherData(
                lastData.coord.lat,
                lastData.coord.lon
            )
        } catch (e: Exception) {
            lastData
        }
    }

    override suspend fun getSavedWeathers(): List<WeatherData> {
        return weatherLocalDataSource.getSavedWeathers()
    }

//    private fun isNetworkAvailable() = networkChecker.getConnectionType() != NetworkChecker.NONE
}