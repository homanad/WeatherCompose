package com.homalab.android.compose.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.homalab.android.compose.weather.data.db.dao.WeatherDataDao
import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity

@Database(entities = [WeatherDataEntity::class], version = 1, exportSchema = true)
@TypeConverters(TypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDataDao: WeatherDataDao
}