package com.homalab.android.compose.weather.presentation.components.charts.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homalab.android.compose.weather.presentation.components.charts.HorizontalLineStyle

val DefaultLineWidth = 4.dp
val DefaultAxisLabelColor = Color(0xFF3D3D3D)
val DefaultAxisColor = Color(0xFF3D3D3D)
val DefaultAxisLabelFontSize = 13.sp
val DefaultAxisThickness = 1.dp
val DefaultContentPadding = 8.dp
val HorizontalLineSpacing = 30.dp
const val MaxChartLabelInOneLine = 3
const val DEFAULT_DURATION = 150
const val DefaultBarWidthRatio = 0.7f

object ChartDefaults {

    fun defaultAnimationOptions() =
        AnimationOptions(isEnabled = false, durationMillis = DEFAULT_DURATION)

    @Immutable
    class AnimationOptions(
        val isEnabled: Boolean,
        val durationMillis: Int,
    ) {
        @Stable
        fun copy(isEnabled: Boolean = this.isEnabled, durationMillis: Int = this.durationMillis) =
            AnimationOptions(isEnabled, durationMillis)
    }

    fun defaultAxisOptions() = AxisOptions(
        axisColor = DefaultAxisColor,
        axisThickness = DefaultAxisThickness,
        axisLabelColor = DefaultAxisLabelColor,
        axisLabelFontSize = DefaultAxisLabelFontSize
    )

    @Immutable
    class AxisOptions(
        val axisColor: Color,
        val axisThickness: Dp,
        val axisLabelColor: Color,
        val axisLabelFontSize: TextUnit
    ) {
        @Stable
        fun copy(
            axisColor: Color = this.axisColor,
            axisThickness: Dp = this.axisThickness,
            axisLabelColor: Color = this.axisLabelColor,
            axisLabelFontSize: TextUnit = this.axisLabelFontSize
        ) = AxisOptions(axisColor, axisThickness, axisLabelColor, axisLabelFontSize)
    }

    fun defaultHorizontalLineOptions() = HorizontalLineOptions(
        showHorizontalLines = true,
        horizontalLineColor = DefaultAxisColor,
        horizontalLineThickness = DefaultAxisThickness,
        horizontalLineSpacing = HorizontalLineSpacing,
        horizontalLineStyle = HorizontalLineStyle.DASH
    )

    @Immutable
    class HorizontalLineOptions(
        val showHorizontalLines: Boolean,
        val horizontalLineColor: Color,
        val horizontalLineThickness: Dp,
        val horizontalLineSpacing: Dp,
        val horizontalLineStyle: HorizontalLineStyle,
    ) {
        @Stable
        fun copy(
            showHorizontalLines: Boolean = this.showHorizontalLines,
            horizontalLineColor: Color = this.horizontalLineColor,
            horizontalLineThickness: Dp = this.horizontalLineThickness,
            horizontalLineSpacing: Dp = this.horizontalLineSpacing,
            horizontalLineStyle: HorizontalLineStyle = this.horizontalLineStyle
        ) = HorizontalLineOptions(
            showHorizontalLines,
            horizontalLineColor,
            horizontalLineThickness,
            horizontalLineSpacing,
            horizontalLineStyle
        )
    }
}