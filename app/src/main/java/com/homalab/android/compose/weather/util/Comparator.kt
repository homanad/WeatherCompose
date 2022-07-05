package com.homalab.android.compose.weather.util

fun Int.isInRange(min: Int, max: Int): Boolean {
    return this in min..max
}