package com.homalab.android.compose.weather.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.weather.domain.entity.subEntity.Main

@Composable
fun MultipleLinesChart(
    modifier: Modifier = Modifier,
    chartData: List<Main>,
//    lineValues: (Main) -> Float,
    verticalAxisValues: List<Float>,
    horizontalAxisLabelColor: Color = DefaultAxisLabelColor,
    horizontalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    verticalAxisLabelColor: Color = DefaultAxisLabelColor,
    verticalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
    axisColor: Color = Color.Gray,
    strokeWidth: Dp = DefaultStrokeWidth,
    strokeColor: Color,
    dotColor: Color
) {
    val chartHeight = HorizontalLineSpacing * verticalAxisValues.size

    Canvas(
        modifier = modifier
            .fillMaxWidth()
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

            drawRect(
                color = axisColor,
                topLeft = Offset(leftAreaWidth.toFloat(), y),
                size = Size(horizontalAxisLength, axisThicknessPx)
            )
        }

        //draw line values
        val barWidth = (drawContext.size.width - leftAreaWidth) / chartData.size
        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - verticalAxisValues.minOf { it }

        var previousOffsetMin: Offset? = null
        var previousOffsetNormal: Offset? = null
        var previousOffsetMax: Offset? = null

        val circleOffsets = mutableListOf<CircleEntity>()

        chartData.forEachIndexed { index, main ->
            var x = barWidth * index
            x += leftAreaWidth

            //draw min
            val currentOffsetMin =
                calculateOffset(x, main.temp_min, minValue, deltaRange, verticalAxisLength)

            val endMin = Offset((currentOffsetMin.x + barWidth.div(2)), currentOffsetMin.y)

            circleOffsets.add(CircleEntity(Color.Green, endMin))

            previousOffsetMin?.let {
                val start = Offset(it.x + barWidth.div(2), it.y)
                drawLine(
                    start = start,
                    end = endMin,
                    color = Color.Green,
                    strokeWidth = strokeWidth.toPx()
                )
            }
            previousOffsetMin = currentOffsetMin
            //

            //draw max
            val currentOffsetMax =
                calculateOffset(x, main.temp_max, minValue, deltaRange, verticalAxisLength)

            val endMax = Offset((currentOffsetMax.x + barWidth.div(2)), currentOffsetMax.y)

            circleOffsets.add(CircleEntity(Color.Red, endMax))

            previousOffsetMax?.let {
                val start = Offset(it.x + barWidth.div(2), it.y)
                drawLine(
                    start = start,
                    end = endMax,
                    color = Color.Red,
                    strokeWidth = strokeWidth.toPx()
                )
            }
            previousOffsetMax = currentOffsetMax
            //

            //draw normal
            val currentOffsetNormal =
                calculateOffset(x, main.temp, minValue, deltaRange, verticalAxisLength)

            val endNormal = Offset((currentOffsetNormal.x + barWidth.div(2)), currentOffsetNormal.y)

            circleOffsets.add(CircleEntity(Color.Yellow, endNormal))

            previousOffsetNormal?.let {
                val start = Offset(it.x + barWidth.div(2), it.y)
                drawLine(
                    start = start,
                    end = endNormal,
                    color = Color.Yellow,
                    strokeWidth = strokeWidth.toPx()
                )
            }
            previousOffsetNormal = currentOffsetNormal

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "test",
                    (currentOffsetNormal.x + barWidth.div(2)),
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