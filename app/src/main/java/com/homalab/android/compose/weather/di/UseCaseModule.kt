package com.homalab.android.compose.weather.di

import com.homalab.android.compose.weather.domain.repository.CityRepository
import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import com.homalab.android.compose.weather.domain.usecase.GetCurrentWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetLastWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.GetSavedWeatherUseCase
import com.homalab.android.compose.weather.domain.usecase.SearchCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCurrentWeatherUseCase(weatherRepository: WeatherRepository) =
        GetCurrentWeatherUseCase(weatherRepository)

    @Provides
    fun provideGetLastWeatherUseCase(weatherRepository: WeatherRepository) =
        GetLastWeatherUseCase(weatherRepository)

    @Provides
    fun provideGetSavedWeatherUseCase(weatherRepository: WeatherRepository) =
        GetSavedWeatherUseCase(weatherRepository)

    @Provides
    fun provideSearchCityUseCase(cityRepository: CityRepository) = SearchCityUseCase(cityRepository)
}