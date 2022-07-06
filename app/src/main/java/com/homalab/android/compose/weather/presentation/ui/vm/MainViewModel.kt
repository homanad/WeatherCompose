package com.homalab.android.compose.weather.presentation.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homalab.android.compose.weather.domain.entity.City
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.domain.usecase.GetCurrentWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetLastWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetSavedCitiesUseCase
import com.homalab.android.compose.weather.domain.usecase.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLastWeatherUseCase: GetLastWeatherUseCase,
    private val getSavedWeatherUseCase: GetSavedCitiesUseCase,
    private val searchCityUseCase: SearchCityUseCase
) : ViewModel() {

    suspend fun getCurrentWeather(id: Int, lat: Double, lon: Double): WeatherData? {
        return try {
            withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                getCurrentWeatherUseCase(
                    GetCurrentWeatherUseCase.GetCurrentWeatherParam(id, lat, lon)
                )
            }
        } catch (e: Exception) {
            getLastWeather()
        }
    }

    suspend fun getLastWeather(): WeatherData? {
        return try {
            withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                getLastWeatherUseCase.invoke(0)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSavedWeathers(): List<City>? {
        return try {
            withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
                getSavedWeatherUseCase.invoke(0)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun search(keyword: String): List<City> {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            searchCityUseCase(keyword)
        }
    }
}