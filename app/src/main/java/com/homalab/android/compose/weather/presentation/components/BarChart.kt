package com.homalab.android.compose.weather.presentation.components

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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.weather.util.Dimension3

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    chartData: List<BarChartData>,
    verticalAxisValues: List<Float>,
    verticalAxisLabelTransform: (Float) -> String,
    horizontalAxisLabelColor: Color = DefaultAxisLabelColor,
    horizontalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    showHorizontalLines: Boolean = true,
    horizontalLineStyle: HorizontalLineStyle = HorizontalLineStyle.DASH,
    verticalAxisLabelColor: Color = DefaultAxisLabelColor,
    verticalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    axisColor: Color = Color.Gray,
    barWidthRatio: Float = DefaultBarWidthRatio
) {
    val chartHeight = HorizontalLineSpacing * verticalAxisValues.size

    Canvas(modifier = modifier.height(chartHeight)) {
        val bottomAreaHeight = horizontalAxisLabelFontSize.toPx()
        val leftAreaWidth =
            (verticalAxisLabelTransform(verticalAxisValues.last()).length * verticalAxisLabelFontSize.toPx()
                .div(1.75)).toInt() + Dimension3.toPx() //TODO fix padding

        val verticalAxisLength = size.height - bottomAreaHeight
        val horizontalAxisLength = size.width - leftAreaWidth

        val distanceBetweenVerticalAxisValues = (verticalAxisLength / (verticalAxisValues.size - 1))

        val axisThicknessPx = 1.dp.toPx()

        //draw horizontal axis
        drawRect(
            color = axisColor,
            topLeft = Offset(leftAreaWidth.toFloat(), verticalAxisLength),
            size = Size(horizontalAxisLength, axisThicknessPx)
        )

        //draw vertical axis
        drawRect(
            color = axisColor,
            topLeft = Offset(leftAreaWidth.toFloat(), 0.0f),
            size = Size(axisThicknessPx, verticalAxisLength)
        )

        //draw horizontal lines & labels
        verticalAxisValues.forEachIndexed { index, fl ->
            val x = leftAreaWidth / 2.toFloat()
            val y = verticalAxisLength - (distanceBetweenVerticalAxisValues).times(index)

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    verticalAxisLabelTransform(fl),
                    x,
                    y + verticalAxisLabelFontSize.toPx() / 2,
                    Paint().apply {
                        textSize = verticalAxisLabelFontSize.toPx()
                        color = verticalAxisLabelColor.toArgb()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            if (showHorizontalLines && index != 0)
                drawLine(
                    start = Offset(leftAreaWidth.toFloat(), y),
                    end = Offset(leftAreaWidth.toFloat() + horizontalAxisLength, y),
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

            drawRect(
                color = barChartData.barColor,
                topLeft = rect.topLeft,
                size = rect.bottomRight.toSize(rect.topLeft)
            )

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    barChartData.label,
                    center,
                    verticalAxisLength + horizontalAxisLabelFontSize.toPx(),
                    Paint().apply {
                        textSize = horizontalAxisLabelFontSize.toPx()
                        color = horizontalAxisLabelColor.toArgb()
                        textAlign = Paint.Align.CENTER
                    }
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

data class BarChartData(
    val barColor: Color,
    val barValue: Float,
    val label: String
)

private const val DefaultBarWidthRatio = 0.7f
