package com.homalab.android.compose.weather.util

object TextTransformer {

    fun transformVisibility(visibility: Int): String {
        val km = visibility.toFloat() / 1000
        return if (km > 1) "$km Km"
        else "$visibility m"
    }
}