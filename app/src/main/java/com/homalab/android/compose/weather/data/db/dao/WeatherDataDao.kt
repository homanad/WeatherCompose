package com.homalab.android.compose.weather.data.db.dao

import androidx.room.Dao
import com.homalab.android.compose.weather.data.common.BaseDao
import com.homalab.android.compose.weather.data.db.entity.WeatherDataEntity

@Dao
abstract class WeatherDataDao : BaseDao<WeatherDataEntity>