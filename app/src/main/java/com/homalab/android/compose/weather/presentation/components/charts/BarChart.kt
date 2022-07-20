package com.homalab.android.compose.weather.presentation.components.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.homalab.android.compose.weather.presentation.components.charts.components.*
import com.homalab.android.compose.weather.presentation.components.charts.entities.BarEntity

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    chartData: List<BarChartData>,
    verticalAxisValues: MutableList<Float> = mutableListOf(),
    verticalAxisLabelTransform: (Float) -> String,
    horizontalAxisOptions: ChartDefaults.AxisOptions = ChartDefaults.defaultAxisOptions(),
    verticalAxisOptions: ChartDefaults.AxisOptions = ChartDefaults.defaultAxisOptions(),
    horizontalLineOptions: ChartDefaults.HorizontalLineOptions = ChartDefaults.defaultHorizontalLineOptions(),
    animationOptions: ChartDefaults.AnimationOptions = ChartDefaults.defaultAnimationOptions(),
    barWidthRatio: Float = DefaultBarWidthRatio,
    contentPadding: Dp = DefaultContentPadding
) {
    if (verticalAxisValues.isEmpty()) verticalAxisValues.addAll(generateVerticaAxisValues(chartData))

    val horizontalAxisThicknessPx = horizontalAxisOptions.axisThickness.toPx()
    val verticalAxisThicknessPx = verticalAxisOptions.axisThickness.toPx()
    val contentPaddingPx = contentPadding.toPx()

    val visibleChartHeight =
        horizontalLineOptions.horizontalLineSpacing * (verticalAxisValues.size - 1)
    val horizontalAxisLabelHeight = contentPadding + horizontalAxisOptions.axisLabelFontSize.toDp()

    val chartHeight = visibleChartHeight + horizontalAxisLabelHeight

    val leftAreaWidth =
        (verticalAxisLabelTransform(verticalAxisValues.last()).length * verticalAxisOptions.axisLabelFontSize.toPx()
            .div(1.75)).toInt() + contentPaddingPx

    var animatedBars by remember {
        mutableStateOf(listOf<BarEntity>())
    }

    Canvas(modifier = modifier.height(chartHeight)) {
        val verticalAxisLength = visibleChartHeight.toPx()
        val horizontalAxisLength = size.width - leftAreaWidth

        val distanceBetweenVerticalAxisValues = (verticalAxisLength / (verticalAxisValues.size - 1))

        //draw horizontal axis
        drawRect(
            color = horizontalAxisOptions.axisColor,
            topLeft = Offset(leftAreaWidth, verticalAxisLength),
            size = Size(horizontalAxisLength, horizontalAxisThicknessPx)
        )

        //draw vertical axis
        drawRect(
            color = verticalAxisOptions.axisColor,
            topLeft = Offset(leftAreaWidth, 0.0f),
            size = Size(verticalAxisThicknessPx, verticalAxisLength)
        )

        //draw horizontal lines & labels
        val verticalValuesTextPaint = Paint().apply {
            textSize = verticalAxisOptions.axisLabelFontSize.toPx()
            color = verticalAxisOptions.axisLabelColor.toArgb()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val horizontalValuesTextPaint = Paint().apply {
            textSize = horizontalAxisOptions.axisLabelFontSize.toPx()
            color = horizontalAxisOptions.axisLabelColor.toArgb()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        verticalAxisValues.forEachIndexed { index, fl ->
            val x = leftAreaWidth / 2.toFloat()
            val y = verticalAxisLength - (distanceBetweenVerticalAxisValues).times(index)

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    verticalAxisLabelTransform(fl),
                    x,
                    y + verticalAxisOptions.axisLabelFontSize.toPx() / 2,
                    verticalValuesTextPaint
                )
            }

            if (horizontalLineOptions.showHorizontalLines && index != 0)
                drawLine(
                    start = Offset(leftAreaWidth, y),
                    end = Offset(leftAreaWidth + horizontalAxisLength, y),
                    color = horizontalLineOptions.horizontalLineColor,
                    strokeWidth = horizontalLineOptions.horizontalLineThickness.toPx(),
                    pathEffect = if (horizontalLineOptions.horizontalLineStyle == HorizontalLineStyle.DASH) PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 5f
                    ) else null
                )
        }

        val barWidth = horizontalAxisLength / chartData.size
        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - minValue

        val rectFs = mutableListOf<BarEntity>()

        chartData.forEachIndexed { index, barChartData ->
            var start = barWidth * index
            start += leftAreaWidth

            val center = start + barWidth / 2

            val calculatedBarWidth = barWidth * barWidthRatio
            val left = center - calculatedBarWidth / 2
            val right = left + calculatedBarWidth

            val rect = calculateRect(
                left = left,
                right = right,
                value = barChartData.barValue,
                minValue = minValue,
                deltaRange = deltaRange,
                verticalAxisLength = verticalAxisLength
            )

            if (animationOptions.isEnabled) {
                rectFs.add(
                    BarEntity(
                        barChartData.barColor,
                        Rect(rect.left, rect.top, rect.right, rect.bottom)
                    )
                )
            } else drawRect(
                color = barChartData.barColor,
                topLeft = rect.topLeft,
                size = rect.bottomRight.toSize(rect.topLeft)
            )

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    barChartData.label,
                    center,
                    verticalAxisLength + horizontalAxisLabelHeight.toPx(),
                    horizontalValuesTextPaint
                )
            }
        }

        animatedBars = rectFs
    }

    animatedBars.filter { it.rect.size.height > 0 }.forEachIndexed { index, bar ->
        AnimatedBar(
            modifier = modifier.height(chartHeight),
            durationMillis = animationOptions.durationMillis,
            delayMillis = index * animationOptions.durationMillis.toLong(),
            barEntity = bar
        )
    }
}

private fun generateVerticaAxisValues(chartData: List<BarChartData>): List<Float> {
    val values = mutableListOf<Float>().apply {
        addAll(chartData.map { it.barValue })
    }
    return generateVerticalValues(values.minOf { it }, values.maxOf { it })
}


private fun Offset.toSize(topLeftOffset: Offset): Size {
    return Size(x - topLeftOffset.x, y - topLeftOffset.y)
}

private fun calculateRect(
    left: Float,
    right: Float,
    value: Float,
    minValue: Float,
    deltaRange: Float,
    verticalAxisLength: Float
): Rect {
    val deltaValue = value - minValue
    val valuePercent = deltaValue / deltaRange
    val barHeightInPixel = valuePercent * verticalAxisLength
    val top = verticalAxisLength - barHeightInPixel

    return Rect(topLeft = Offset(left, top), Offset(right, verticalAxisLength))
}

data class BarChartData(
    val barColor: Color,
    val barValue: Float,
    val label: String
)