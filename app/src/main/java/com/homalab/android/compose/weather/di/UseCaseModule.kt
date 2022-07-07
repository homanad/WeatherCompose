package com.homalab.android.compose.weather.di

import com.homalab.android.compose.weather.domain.repository.CityRepository
import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import com.homalab.android.compose.weather.domain.usecase.*
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
        GetSavedCitiesUseCase(weatherRepository)

    @Provides
    fun provideSearchCityUseCase(cityRepository: CityRepository) = SearchCityUseCase(cityRepository)

    @Provides
    fun provideGetForecastDataUseCase(weatherRepository: WeatherRepository) =
        GetForecastDataUseCase(weatherRepository)
}