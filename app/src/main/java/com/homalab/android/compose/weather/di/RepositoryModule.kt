package com.homalab.android.compose.weather.di

import com.homalab.android.compose.weather.data.datasource.*
import com.homalab.android.compose.weather.data.repository.CityRepositoryImpl
import com.homalab.android.compose.weather.data.repository.WeatherRepositoryImpl
import com.homalab.android.compose.weather.domain.repository.CityRepository
import com.homalab.android.compose.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @RemoteDataSource
    @Binds
    fun bindWeatherRemoteDataSource(remoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherDataSource

    @LocalDataSource
    @Binds
    fun bindWeatherLocalDataSource(localDataSourceImpl: WeatherLocalDataSourceImpl): WeatherDataSource

    @Binds
    fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindCityDataSource(cityDataSourceImpl: CityDataSourceImpl): CityDataSource

    @Binds
    fun bindCityRepository(cityRepositoryImpl: CityRepositoryImpl): CityRepository
}