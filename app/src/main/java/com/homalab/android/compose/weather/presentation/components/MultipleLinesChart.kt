package com.homalab.android.compose.weather.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


@Composable
fun MultipleLinesChart(
    modifier: Modifier = Modifier,
    chartData: List<MultipleChartData>,
    verticalAxisValues: List<Float>,
    horizontalAxisLabelColor: Color = DefaultAxisLabelColor,
    horizontalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    showHorizontalLines: Boolean = true,
    horizontalLineStyle: HorizontalLineStyle = HorizontalLineStyle.DASH,
    verticalAxisLabelColor: Color = DefaultAxisLabelColor,
    verticalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    axisColor: Color = Color.Gray,
    strokeWidth: Dp = DefaultStrokeWidth
) {
    val chartHeight = HorizontalLineSpacing * verticalAxisValues.size

    Canvas(
        modifier = modifier
            .height(chartHeight)
    ) {
        val bottomAreaHeight = horizontalAxisLabelFontSize.toPx()
        val verticalAxisLength = size.height - bottomAreaHeight

        println("----------------")

        val leftAreaWidth =
            (verticalAxisValues[verticalAxisValues.size - 1].toString().length * verticalAxisLabelFontSize.toPx()
                .div(1.75)).toInt()
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

            println("-------lineY: $y")

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    fl.toString(),
                    x,
                    y + verticalAxisLabelFontSize.toPx() / 2,
                    Paint().apply {
                        textSize = verticalAxisLabelFontSize.toPx()
                        color = verticalAxisLabelColor.toArgb()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

//            drawRect(
//                color = axisColor,
//                topLeft = Offset(leftAreaWidth.toFloat(), y),
//                size = Size(horizontalAxisLength, axisThicknessPx)
//            )
            //don't draw min line
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

        //draw line values
        val barWidth = (drawContext.size.width - leftAreaWidth) / chartData.maxOf { it.values.size }

        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - verticalAxisValues.minOf { it }

        val circleOffsets = mutableListOf<CircleEntity>()
        val textOffsets = mutableListOf<TextEntity>()

        chartData.forEach { multipleChartData ->
            var previousOffset: Offset? = null

            multipleChartData.values.forEachIndexed { index, multipleChartValue ->
                var x = barWidth * index
                x += leftAreaWidth

                val currentOffset =
                    calculateOffset(
                        x,
                        multipleChartValue.value,
                        minValue,
                        deltaRange,
                        verticalAxisLength
                    )

                val endOffset = Offset((currentOffset.x + barWidth.div(2)), currentOffset.y)

                circleOffsets.add(CircleEntity(multipleChartData.dotColor, endOffset))

                val newTextEntity = TextEntity(multipleChartValue.label, currentOffset)
                if (!textOffsets.contains(newTextEntity)) textOffsets.add(newTextEntity)

                previousOffset?.let {
                    val start = Offset(it.x + barWidth.div(2), it.y)
                    drawLine(
                        start = start,
                        end = endOffset,
                        color = multipleChartData.lineColor,
                        strokeWidth = strokeWidth.toPx()
                    )
                }
                previousOffset = currentOffset
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            textOffsets.forEach {
                drawText(
                    it.text,
                    (it.offset.x + barWidth.div(2)),
                    verticalAxisLength + horizontalAxisLabelFontSize.toPx(),
                    Paint().apply {
                        textSize = horizontalAxisLabelFontSize.toPx()
                        color = horizontalAxisLabelColor.toArgb()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }

        circleOffsets.forEach {
            drawCircle(
                color = it.color,
                center = it.offset,
                radius = strokeWidth.times(1.5f).toPx()
            )
        }
    }
}

fun calculateOffset(
    x: Float,
    value: Float,
    minValue: Float,
    deltaRange: Float,
    verticalAxisLength: Float
): Offset {
    val deltaValue = value - minValue
    val valuePercent = deltaValue / deltaRange

    val barHeightInPixel = valuePercent * verticalAxisLength
    val y = verticalAxisLength - barHeightInPixel

    println("-------line value: $value")
    println("-------verticalAxisLength: $verticalAxisLength")
    println("-------valuePercent: $valuePercent")
    println("-------barHeightInPixel: $barHeightInPixel")
    println("-------y: $y")
    println("----------------")

    return Offset(x, y)
}

data class CircleEntity(val color: Color, val offset: Offset)
data class TextEntity(val text: String, val offset: Offset)

data class MultipleChartData(
    val dotColor: Color,
    val lineColor: Color,
    val values: List<MultipleChartValue>
)

data class MultipleChartValue(
    val label: String,
    val value: Float
)

enum class HorizontalLineStyle {
    DASH, STROKE
}