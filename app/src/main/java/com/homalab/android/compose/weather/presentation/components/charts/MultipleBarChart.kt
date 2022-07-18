package com.homalab.android.compose.weather.presentation.components.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.homalab.android.compose.weather.presentation.components.charts.components.toDp
import com.homalab.android.compose.weather.presentation.components.charts.components.toPx

@Composable
fun MultipleBarsChart(
    modifier: Modifier = Modifier,
    chartData: List<MultipleBarsChartData>,
    verticalAxisValues: List<Float>,
    verticalAxisLabelTransform: (Float) -> String,
    horizontalAxisLabelColor: Color = DefaultAxisLabelColor,
    horizontalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    showHorizontalLines: Boolean = true,
    horizontalLineSpacing: Dp = HorizontalLineSpacing,
    horizontalLineStyle: HorizontalLineStyle = HorizontalLineStyle.DASH,
    verticalAxisLabelColor: Color = DefaultAxisLabelColor,
    verticalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    axisColor: Color = Color.Gray,
    barWidthRatio: Float = DefaultBarWidthRatio,
    axisThickness: Dp = DefaultAxisThickness,
    contentPadding: Dp = DefaultContentPadding
) {
    val axisThicknessPx = axisThickness.toPx()
    val contentPaddingPx = contentPadding.toPx()

    val visibleChartHeight = horizontalLineSpacing * (verticalAxisValues.size - 1)
    val horizontalAxisLabelHeight = contentPadding + horizontalAxisLabelFontSize.toDp()

    val chartHeight = visibleChartHeight + horizontalAxisLabelHeight

    val leftAreaWidth =
        (verticalAxisLabelTransform(verticalAxisValues.last()).length * verticalAxisLabelFontSize.toPx()
            .div(1.75)).toInt() + contentPaddingPx

    Canvas(modifier = modifier.height(chartHeight)) {
        val verticalAxisLength = visibleChartHeight.toPx()
        val horizontalAxisLength = size.width - leftAreaWidth

        val distanceBetweenVerticalAxisValues = (verticalAxisLength / (verticalAxisValues.size - 1))

        //draw horizontal axis
        drawRect(
            color = axisColor,
            topLeft = Offset(leftAreaWidth, verticalAxisLength),
            size = Size(horizontalAxisLength, axisThicknessPx)
        )

        //draw vertical axis
        drawRect(
            color = axisColor,
            topLeft = Offset(leftAreaWidth, 0.0f),
            size = Size(axisThicknessPx, verticalAxisLength)
        )

        //draw horizontal lines & labels
        val verticalValuesTextPaint = Paint().apply {
            textSize = verticalAxisLabelFontSize.toPx()
            color = verticalAxisLabelColor.toArgb()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val horizontalValuesTextPaint = Paint().apply {
            textSize = horizontalAxisLabelFontSize.toPx()
            color = horizontalAxisLabelColor.toArgb()
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
                    y + verticalAxisLabelFontSize.toPx() / 2,
                    verticalValuesTextPaint
                )
            }

            if (showHorizontalLines && index != 0)
                drawLine(
                    start = Offset(leftAreaWidth, y),
                    end = Offset(leftAreaWidth + horizontalAxisLength, y),
                    color = axisColor,
                    strokeWidth = axisThicknessPx,
                    pathEffect = if (horizontalLineStyle == HorizontalLineStyle.DASH) PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 5f
                    ) else null
                )
        }

        val barWidth = (drawContext.size.width - leftAreaWidth) / chartData.size
        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - minValue

        chartData.forEachIndexed { index, barChartData ->
            var start = barWidth * index
            start += leftAreaWidth + ((barWidth - barWidth * barWidthRatio) / 2)

            val center = start + barWidth / 2

            val calculatedOneBarWidth = (barWidth * barWidthRatio / barChartData.values.size)

            barChartData.values.forEachIndexed { valueIndex, barsChartValue ->
                val startX = start + calculatedOneBarWidth * valueIndex

                val rect = calculateRect(
                    left = startX,
                    right = startX + calculatedOneBarWidth,
                    value = barsChartValue.value,
                    minValue = minValue,
                    deltaRange = deltaRange,
                    verticalAxisLength = verticalAxisLength
                )

                drawRect(
                    color = barsChartValue.barColor,
                    topLeft = rect.topLeft,
                    size = rect.bottomRight.toSize(rect.topLeft)
                )
            }

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    barChartData.label,
                    center,
                    verticalAxisLength + horizontalAxisLabelHeight.toPx(),
                    horizontalValuesTextPaint
                )
            }
        }
    }
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

data class MultipleBarsChartData(
    val values: List<MultipleBarsChartValue>,
    val label: String
)

data class MultipleBarsChartValue(
    val barColor: Color,
    val value: Float
)