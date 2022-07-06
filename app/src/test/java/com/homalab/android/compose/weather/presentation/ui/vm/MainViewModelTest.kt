package com.homalab.android.compose.weather.presentation.ui.vm

import com.homalab.android.compose.weather.data.datasource.WeatherLocalDataSource
import com.homalab.android.compose.weather.data.datasource.WeatherRemoteDataSource
import com.homalab.android.compose.weather.data.repository.WeatherRepositoryImpl
import com.homalab.android.compose.weather.domain.entity.*
import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import com.homalab.android.compose.weather.domain.usecase.GetCurrentWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetLastWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetSavedCitiesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private val standardTestDispatcher = StandardTestDispatcher()

    @Test
    fun shouldReturnRemoteWeather() = runTest(standardTestDispatcher) {
        val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
            WeatherRemoteDataSourceImpl(),
            WeatherLocalDataSourceImpl()
        )

        val useCase = GetCurrentWeatherUseCase(weatherRepository)
        val weatherData =
            useCase.invoke(GetCurrentWeatherUseCase.GetCurrentWeatherParam(10, 10.0, 10.0))
        Assert.assertEquals(weatherData, getRemoteWeatherData(10.0, 10.0))
    }

    @Test
    fun shouldReturnLocalWeather() = runTest(standardTestDispatcher) {
        val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
            ExceptionRemoteDataSourceImpl(),
            WeatherLocalDataSourceImpl()
        )

        val useCase = GetCurrentWeatherUseCase(weatherRepository)
        val weatherData =
            useCase.invoke(GetCurrentWeatherUseCase.GetCurrentWeatherParam(10, 10.0, 10.0))

        Assert.assertEquals(weatherData, getLocalWeatherData(10))
    }

    @Test
    fun shouldReturnLastWeather() = runTest(standardTestDispatcher) {
        val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
            ExceptionRemoteDataSourceImpl(),
            WeatherLocalDataSourceImpl()
        )

        val useCase = GetLastWeatherUseCase(weatherRepository)
        val weatherData = useCase.invoke(0)
        Assert.assertEquals(weatherData, getLocalWeatherData(0))
    }

    @Test
    fun shouldReturnSavedWeathers() = runTest(standardTestDispatcher) {
        val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
            ExceptionRemoteDataSourceImpl(),
            WeatherLocalDataSourceImpl()
        )

        val useCase = GetSavedCitiesUseCase(weatherRepository)
        val data = useCase.invoke(0)
        Assert.assertEquals(data, listOf<WeatherData>())
    }
}

class WeatherLocalDataSourceImpl : WeatherLocalDataSource {
    override suspend fun getSavedWeatherData(id: Int): WeatherData {
        return getLocalWeatherData(id)
    }

    override suspend fun saveWeatherData(weatherData: WeatherData): Long {
        return -1
    }

    override suspend fun getLastWeatherData(): WeatherData {
        return getLocalWeatherData(0)
    }

    override suspend fun getSavedCities(): List<City> {
        return listOf()
    }
}

class WeatherRemoteDataSourceImpl : WeatherRemoteDataSource {
    override suspend fun getCurrentWeatherData(lat: Double, lon: Double): WeatherData {
        return getRemoteWeatherData(lat, lon)
    }
}

class ExceptionRemoteDataSourceImpl : WeatherRemoteDataSource {
    override suspend fun getCurrentWeatherData(lat: Double, lon: Double): WeatherData {
        throw Exception("Network unavailable")
    }
}

private fun getLocalWeatherData(id: Int): WeatherData {
    return WeatherData(
        coord = Coord(10.0, 10.0),
        weather = listOf(),
        base = "",
        main = Main(10f, 10f, 10f, 10f, 10f, 10f),
        visibility = 0,
        wind = Wind(10f, 360, 10f),
        dt = 10L,
        sys = Sys(10, id, "local", 100L, 100L),
        timeZone = 1,
        id = id,
        name = "local",
        cod = 1
    )
}

private fun getRemoteWeatherData(lat: Double, lon: Double): WeatherData {
    return WeatherData(
        coord = Coord(lat, lon),
        weather = listOf(),
        base = "",
        main = Main(10f, 10f, 10f, 10f, 10f, 10f),
        visibility = 0,
        wind = Wind(10f, 360, 10f),
        dt = 10L,
        sys = Sys(10, 1000, "remote", 100L, 100L),
        timeZone = 1,
        id = 1000,
        name = "remote",
        cod = 1
    )
}
