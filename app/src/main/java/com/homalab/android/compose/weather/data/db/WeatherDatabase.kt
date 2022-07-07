package com.homalab.android.compose.weather.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.homalab.android.compose.weather.data.db.dao.WeatherDataDao
import com.homalab.android.compose.weather.data.db.entity.ForecastDataEntity
import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity

@Database(
    entities = [WeatherDataEntity::class, ForecastDataEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(TypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDataDao: WeatherDataDao
}