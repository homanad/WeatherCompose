package com.homalab.android.compose.weather.util

import android.os.Bundle
import androidx.navigation.NavType

@JvmField
val DoubleType: NavType<Double> = object : NavType<Double>(false) {
    override val name: String
        get() = "float"

    override fun put(bundle: Bundle, key: String, value: Double) {
        bundle.putDouble(key, value)
    }

    @Suppress("DEPRECATION")
    override fun get(bundle: Bundle, key: String): Double {
        return bundle[key] as Double
    }

    override fun parseValue(value: String): Double {
        return value.toDouble()
    }
}