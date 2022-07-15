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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

@Composable
fun MultipleLinesChart(
    modifier: Modifier = Modifier,
    chartData: List<MultipleChartData>,
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
    strokeWidth: Dp = DefaultStrokeWidth,
    drawCirclePoint: Boolean = true,
    axisThickness: Dp = DefaultAxisThickness,
    contentPadding: Dp = DefaultContentPadding
) {
    val visibleChartHeight = horizontalLineSpacing * (verticalAxisValues.size - 1)
    val horizontalAxisLabelHeight = contentPadding + horizontalAxisLabelFontSize.toDp()

    val chartLabelHeight = contentPadding + horizontalAxisLabelFontSize.toDp()
    val totalChartLabelHeight = chartLabelHeight *
            ceil(chartData.size.toFloat() / MaxChartLabelInOneLine)

    val chartHeight = visibleChartHeight + horizontalAxisLabelHeight + totalChartLabelHeight

    val horizontalAxisLabelFontSizePx = horizontalAxisLabelFontSize.toPx()
    val verticalAxisLabelFontSizePx = verticalAxisLabelFontSize.toPx()

    val contentPaddingPx = contentPadding.toPx()
    val axisThicknessPx = axisThickness.toPx()

    val horizontalLabelAreaY =
        (visibleChartHeight + horizontalAxisLabelHeight).toPx()
//    val chartLabelAreaY =
//        (visibleChartHeight + horizontalAxisLabelHeight + chartLabelHeight).toPx()

    val leftAreaWidth =
        (verticalAxisLabelTransform(verticalAxisValues.last()).length * verticalAxisLabelFontSizePx
            .div(1.75)).toInt() + contentPaddingPx

    val verticalValuesPaint = Paint().apply {
        textSize = verticalAxisLabelFontSizePx
        color = verticalAxisLabelColor.toArgb()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    val horizontalValuesPaint = Paint().apply {
        textSize = horizontalAxisLabelFontSizePx
        color = horizontalAxisLabelColor.toArgb()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    Canvas(modifier = modifier.height(chartHeight)) {
        val verticalAxisHeight = visibleChartHeight.toPx()
        val horizontalAxisWidth = size.width - leftAreaWidth
        val verticalAxisValuesDistance = (verticalAxisHeight / (verticalAxisValues.size - 1))

        //draw horizontal axis
        drawRect(
            color = axisColor,
            topLeft = Offset(leftAreaWidth, verticalAxisHeight),
            size = Size(horizontalAxisWidth, axisThicknessPx)
        )

        //draw vertical axis
        drawRect(
            color = axisColor,
            topLeft = Offset(leftAreaWidth, 0.0f),
            size = Size(axisThicknessPx, verticalAxisHeight)
        )

        //draw horizontal lines & labels
        verticalAxisValues.forEachIndexed { index, value ->
            val x = leftAreaWidth / 2.toFloat()
            val y = verticalAxisHeight - (verticalAxisValuesDistance).times(index)

            drawContext.canvas.nativeCanvas.run {
                drawText(
                    verticalAxisLabelTransform(value),
                    x,
                    y + verticalAxisLabelFontSizePx / 2,
                    verticalValuesPaint
                )
            }

            //don't draw min line
            if (showHorizontalLines && index != 0)
                drawLine(
                    start = Offset(leftAreaWidth, y),
                    end = Offset(size.width, y),
                    color = axisColor,
                    strokeWidth = axisThicknessPx,
                    pathEffect = if (horizontalLineStyle == HorizontalLineStyle.DASH) PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 5f
                    ) else null
                )
        }

        //draw line values
        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - minValue

        val circleOffsets = mutableListOf<CircleEntity>()
        val textOffsets = mutableListOf<TextEntity>()

        chartData.forEachIndexed { i, multipleChartData ->
            var previousOffset: Offset? = null

            val barWidth = (size.width - leftAreaWidth) / multipleChartData.values.size

            multipleChartData.values.forEachIndexed { index, multipleChartValue ->
                var x = barWidth * index
                x += leftAreaWidth

                val currentOffset = calculateOffset(
                    x,
                    multipleChartValue.value,
                    minValue,
                    deltaRange,
                    verticalAxisHeight
                )

                val endOffset = Offset((currentOffset.x + barWidth.div(2)), currentOffset.y)

                if (drawCirclePoint) circleOffsets.add(
                    CircleEntity(
                        multipleChartData.dotColor,
                        endOffset,
                        multipleChartData.dotRatio
                    )
                )

                val newTextEntity = TextEntity(
                    multipleChartValue.label,
                    currentOffset.copy(x = x + barWidth.div(2))
                )
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

            val labelRectPaint = Paint()
            drawContext.canvas.nativeCanvas.apply {
                val width = horizontalAxisWidth / MaxChartLabelInOneLine
                var x = width * (i % MaxChartLabelInOneLine)
                x += leftAreaWidth

                val part = width.div(4)

                val y =
                    verticalAxisHeight + horizontalAxisLabelHeight.toPx() + chartLabelHeight.toPx() *
                            ceil((i + 1).toFloat() / MaxChartLabelInOneLine)

                x += part

                labelRectPaint.color = multipleChartData.lineColor.toArgb()
                drawRect(
                    x,
                    y - horizontalAxisLabelFontSizePx,
                    x + part,
                    y + horizontalAxisLabelFontSizePx / 2,
                    labelRectPaint
                )

                val textWidth = multipleChartData.label.length * verticalAxisLabelFontSizePx

                drawText(
                    multipleChartData.label,
                    (x + part + textWidth / 2),
                    y,
                    horizontalValuesPaint
                )
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            textOffsets.forEach {
                drawText(
                    it.text,
                    it.offset.x,
                    horizontalLabelAreaY,
                    horizontalValuesPaint
                )
            }
        }

        circleOffsets.forEach {
            drawCircle(
                color = it.color,
                center = it.offset,
                radius = strokeWidth.times(it.ratio).toPx()
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
    return Offset(x, y)
}

data class CircleEntity(val color: Color, val offset: Offset, val ratio: Float)
data class TextEntity(val text: String, val offset: Offset)

data class MultipleChartData(
    val dotColor: Color,
    val lineColor: Color,
    val values: List<MultipleChartValue>,
    val label: String,
    val dotRatio: Float = 1.5f
)

data class MultipleChartValue(
    val label: String,
    val value: Float
)

enum class HorizontalLineStyle {
    DASH, STROKE
}

@Composable
fun Dp.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toDp() = LocalDensity.current.run { this@toDp.toDp() }

val DefaultStrokeWidth = 4.dp
val DefaultAxisLabelColor = Color(0xFF3D3D3D)
val DefaultAxisLabelFontSize = 13.sp
val DefaultAxisThickness = 1.dp
val DefaultContentPadding = 8.dp

val HorizontalLineSpacing = 30.dp

val MaxChartLabelInOneLine = 3