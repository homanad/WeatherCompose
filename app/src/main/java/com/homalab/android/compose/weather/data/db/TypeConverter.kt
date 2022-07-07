package com.homalab.android.compose.weather.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.homalab.android.compose.weather.domain.entity.subEntity.*

class TypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun convertCoordToString(coord: Coord): String {
        return gson.toJson(coord)
    }

    @TypeConverter
    fun convertStringToCoord(json: String): Coord {
        return gson.fromJson(json, Coord::class.java)
    }

    @TypeConverter
    fun convertWeatherToString(weather: List<WeatherItem>): String {
        return gson.toJson(weather)
    }

    @TypeConverter
    fun convertStringToWeather(json: String): List<WeatherItem> {
        val myType = object : TypeToken<List<WeatherItem>>() {}.type
        return gson.fromJson(json, myType)
    }

    @TypeConverter
    fun convertMainToString(main: Main): String {
        return gson.toJson(main)
    }

    @TypeConverter
    fun convertStringToMain(json: String): Main {
        return gson.fromJson(json, Main::class.java)
    }

    @TypeConverter
    fun convertWindToString(wind: Wind): String {
        return gson.toJson(wind)
    }

    @TypeConverter
    fun convertStringToWind(json: String): Wind {
        return gson.fromJson(json, Wind::class.java)
    }

    @TypeConverter
    fun convertCloudToString(cloud: Cloud): String {
        return gson.toJson(cloud)
    }

    @TypeConverter
    fun convertStringToCloud(json: String): Cloud {
        return gson.fromJson(json, Cloud::class.java)
    }

    @TypeConverter
    fun convertSysToString(sys: Sys): String {
        return gson.toJson(sys)
    }

    @TypeConverter
    fun convertStringToSys(json: String): Sys {
        return gson.fromJson(json, Sys::class.java)
    }
}