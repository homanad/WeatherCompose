package com.homalab.android.compose.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homalab.android.compose.weather.domain.entity.WeatherData
import com.homalab.android.compose.weather.domain.usecase.GetCurrentWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetLastWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getLastWeatherUseCase: GetLastWeatherUseCase
) : ViewModel() {

    suspend fun getCurrentWeather(id: Int, lat: Float, lon: Float): WeatherData {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            getCurrentWeatherUseCase(GetCurrentWeatherUseCase.GetCurrentWeatherParam(id, lat, lon))
        }
    }

    suspend fun getLastWeather(): WeatherData {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            getLastWeatherUseCase.invoke(0)
        }
    }
}