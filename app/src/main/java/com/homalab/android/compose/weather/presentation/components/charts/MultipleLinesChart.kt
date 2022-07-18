package com.homalab.android.compose.weather.presentation.components.charts

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay
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
    contentPadding: Dp = DefaultContentPadding,
    animationOptions: ChartDefaults.AnimationOptions = ChartDefaults.defaultAnimationOptions()
) {
    val visibleChartHeight = horizontalLineSpacing * (verticalAxisValues.size - 1)
    val horizontalAxisLabelHeight = contentPadding + horizontalAxisLabelFontSize.toDp()

    val chartLabelLineHeight = contentPadding + horizontalAxisLabelFontSize.toDp()
    val totalChartLabelHeight = chartLabelLineHeight *
            ceil(chartData.size.toFloat() / MaxChartLabelInOneLine)

    val chartHeight = visibleChartHeight + horizontalAxisLabelHeight + totalChartLabelHeight

    val horizontalAxisLabelFontSizePx = horizontalAxisLabelFontSize.toPx()
    val verticalAxisLabelFontSizePx = verticalAxisLabelFontSize.toPx()

    val contentPaddingPx = contentPadding.toPx()
    val axisThicknessPx = axisThickness.toPx()

    val horizontalLabelAreaY =
        (visibleChartHeight + horizontalAxisLabelHeight).toPx()
    val chartLabelAreaBaseY =
        (visibleChartHeight + horizontalAxisLabelHeight).toPx()

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

    var animatedLines by remember {
        mutableStateOf(listOf<Line>())
    }
    var animatedCircles by remember {
        mutableStateOf(listOf<CircleEntity>())
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

        val circleEntities = mutableListOf<CircleEntity>()
        val textOffsets = mutableListOf<TextEntity>()

        val lines = mutableListOf<Line>()

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

                if (drawCirclePoint) circleEntities.add(
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

                    if (animationOptions.isEnabled) lines.add(
                        Line(
                            start = start,
                            end = endOffset,
                            color = multipleChartData.lineColor,
                            strokeWidth = strokeWidth
                        )
                    ) else drawLine(
                        start = start,
                        end = endOffset,
                        color = multipleChartData.lineColor,
                        strokeWidth = strokeWidth.toPx()
                    )
                }
                previousOffset = currentOffset
            }

            val labelRectPaint = Paint().apply { isAntiAlias = true }
            drawContext.canvas.nativeCanvas.apply {
                val width =
                    if (chartData.size >= MaxChartLabelInOneLine) horizontalAxisWidth / MaxChartLabelInOneLine
                    else horizontalAxisWidth / chartData.size
                var x = width * (i % MaxChartLabelInOneLine)
                x += leftAreaWidth

                val y = chartLabelAreaBaseY + chartLabelLineHeight.toPx() *
                        ceil((i + 1).toFloat() / MaxChartLabelInOneLine)

                labelRectPaint.color = multipleChartData.lineColor.toArgb()
                val startRect = x + contentPaddingPx
                val endRect = startRect + width / 4
                drawRect(
                    startRect,
                    y - horizontalAxisLabelFontSizePx,
                    endRect,
                    y + horizontalAxisLabelFontSizePx / 2,
                    labelRectPaint
                )

                val textWidth =
                    (multipleChartData.label.length * verticalAxisLabelFontSizePx).div(1.75)
                        .toFloat()
                drawText(
                    multipleChartData.label,
                    (endRect + contentPaddingPx + textWidth / 2),
                    y,
                    horizontalValuesPaint
                )
            }

            if (animationOptions.isEnabled) {
                animatedLines = lines
                animatedCircles = circleEntities
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

        if (!animationOptions.isEnabled) circleEntities.forEach {
            drawCircle(
                color = it.color,
                center = it.offset,
                radius = strokeWidth.times(it.ratio).toPx()
            )
        }
    }

    animatedLines.forEachIndexed { index, line ->
        AnimatedLine(
            modifier = modifier.height(chartHeight),
            index = index,
            durationMillis = animationOptions.durationMillis,
            line = line
        )
    }

    animatedCircles.forEachIndexed { index, circleEntity ->
        AnimatedCircle(
            modifier = modifier.height(chartHeight),
            index = index,
            durationMillis = animationOptions.durationMillis,
            strokeWidth = strokeWidth,
            circleEntity = circleEntity
        )
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

data class Line(val color: Color, val strokeWidth: Dp, val start: Offset, val end: Offset)

@Composable
fun AnimatedLine(modifier: Modifier, index: Int, durationMillis: Long, line: Line) {
    val animatable = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = null, block = {
        delay((index + 1) * durationMillis)
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMillis.toInt(), easing = LinearEasing)
        )
    })

    Canvas(modifier = modifier, onDraw = {
        drawLine(
            color = line.color,
            start = line.start,
            end = Offset(
                (line.end.x * animatable.value) + line.start.x * (1f - animatable.value),
                (line.end.y * animatable.value) + line.start.y * (1f - animatable.value)
            ),
            strokeWidth = line.strokeWidth.toPx()
        )
    })
}

@Composable
fun AnimatedCircle(
    modifier: Modifier,
    index: Int,
    durationMillis: Long,
    strokeWidth: Dp,
    circleEntity: CircleEntity
) {
    val animatable = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = null, block = {
        delay(index * durationMillis)
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMillis.toInt(), easing = LinearEasing)
        )
    })

    Canvas(modifier = modifier, onDraw = {
        drawCircle(
            color = circleEntity.color,
            center = circleEntity.offset,
            radius = strokeWidth.times(circleEntity.ratio).toPx() * animatable.value
        )
    })
}


@Composable
fun Dp.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toDp() = LocalDensity.current.run { this@toDp.toDp() }