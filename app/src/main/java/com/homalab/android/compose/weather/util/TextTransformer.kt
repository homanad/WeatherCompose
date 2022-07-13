package com.homalab.android.compose.weather.util

private const val C_DEGREE_PATTERN = "%s °C"
private const val KM_PATTERN = "%s Km"
private const val M_PATTERN = "%s m"
private const val MM_PATTERN = "%smm"
private const val C_DEGREE_MIN_MAX_PATTERN = "%1s °C / %2s °C"
private const val CONDITION_PATTERN = "%1s / %2s"
private const val WIND_PATTERN = "%1s m/s - %2s°"
private const val HPA_PATTERN = "%s hPa"

fun transformVisibility(visibility: Int): String {
    val km = visibility.toFloat() / 1000
    return if (km > 1) KM_PATTERN.format(km)
    else M_PATTERN.format(visibility)
}

fun formatCDegree(degree: Float): String {
    return if (degree.toInt().toFloat() == degree) {
        C_DEGREE_PATTERN.format(degree.toInt())
    } else C_DEGREE_PATTERN.format(degree)
}

fun formatMm(value: Float): String {
    return if (value.toInt().toFloat() == value) {
        MM_PATTERN.format(value.toInt())
    } else MM_PATTERN.format(value)
}

fun formatMinMaxDegree(min: Float, max: Float): String {
    val minValue = if (min.toInt().toFloat() == min) min.toInt() else min
    val maxValue = if (max.toInt().toFloat() == max) max.toInt() else max
    return C_DEGREE_MIN_MAX_PATTERN.format(minValue, maxValue)
}

fun formatCondition(main: String, description: String): String {
    return CONDITION_PATTERN.format(main, description)
}

fun formatWind(speed: Float, degree: Int): String {
    return WIND_PATTERN.format(speed, degree)
}

fun formatHPa(value: Int): String {
    return HPA_PATTERN.format(value)
}