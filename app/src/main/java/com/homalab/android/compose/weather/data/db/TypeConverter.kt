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
    fun convertCloudsToString(clouds: Clouds): String {
        return gson.toJson(clouds)
    }

    @TypeConverter
    fun convertStringToClouds(json: String): Clouds {
        return gson.fromJson(json, Clouds::class.java)
    }

    @TypeConverter
    fun convertRainOrSnowToString(rain: RainOrSnow?): String {
        return gson.toJson(rain)
    }

    @TypeConverter
    fun convertStringToRainOrSnow(json: String): RainOrSnow? {
        return gson.fromJson(json, RainOrSnow::class.java)
    }

    @TypeConverter
    fun convertSysToString(sys: Sys): String {
        return gson.toJson(sys)
    }

    @TypeConverter
    fun convertStringToSys(json: String): Sys {
        return gson.fromJson(json, Sys::class.java)
    }

    @TypeConverter
    fun convertForecastToString(forecastItem: List<ForecastItem>): String {
        return gson.toJson(forecastItem)
    }

    @TypeConverter
    fun convertStringToForecast(json: String): List<ForecastItem> {
        val myType = object : TypeToken<List<ForecastItem>>() {}.type
        return gson.fromJson(json, myType)
    }

    @TypeConverter
    fun convertForecastCityToString(forecastCity: ForecastCity): String {
        return gson.toJson(forecastCity)
    }

    @TypeConverter
    fun convertStringToForecastCity(json: String): ForecastCity {
        return gson.fromJson(json, ForecastCity::class.java)
    }
}