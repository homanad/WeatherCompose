package com.homalab.android.compose.weather.di

import android.content.Context
import androidx.room.Room
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.homalab.android.compose.weather.data.api.ApiFactory
import com.homalab.android.compose.weather.data.api.service.WeatherService
import com.homalab.android.compose.weather.data.db.WeatherDatabase
import com.homalab.android.compose.weather.data.util.NetworkChecker
import com.homalab.android.compose.weather.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase::class.java.simpleName
        ).build()
    }

    @Provides
    fun provideWeatherDataDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDataDao

    @Provides
    fun provideForecastDataDao(weatherDatabase: WeatherDatabase) = weatherDatabase.forecastDataDao

    @Provides
    fun provideWeatherService() = ApiFactory.createRetrofit<WeatherService>()

    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context) = NetworkChecker(context)

    @Provides
    fun provideAlgoliaClient(): ClientSearch = ClientSearch(
        applicationID = ApplicationID(Constants.ALGOLIA_APP_ID),
        apiKey = APIKey(Constants.ALGOLIA_API_KEY)
    )
}