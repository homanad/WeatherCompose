package com.homalab.android.compose.weather.presentation.components.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DefaultStrokeWidth = 4.dp
val DefaultAxisLabelColor = Color(0xFF3D3D3D)
val DefaultAxisLabelFontSize = 13.sp
val DefaultAxisThickness = 1.dp
val DefaultContentPadding = 8.dp
val HorizontalLineSpacing = 30.dp
const val MaxChartLabelInOneLine = 3
const val DEFAULT_DURATION = 150

object ChartDefaults {

    fun defaultAnimationOptions() =
        AnimationOptions(isEnabled = false, durationMillis = DEFAULT_DURATION.toLong())

    @Immutable
    class AnimationOptions(
        val isEnabled: Boolean,
        val durationMillis: Long,
    ) {
        @Stable
        fun copy(isEnabled: Boolean = this.isEnabled, durationMillis: Long = this.durationMillis) =
            AnimationOptions(isEnabled, durationMillis)
    }
}