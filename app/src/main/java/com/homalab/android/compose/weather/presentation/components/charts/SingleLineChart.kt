//package com.homalab.android.compose.weather.presentation.components
//
//import android.graphics.Paint
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.nativeCanvas
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.homalab.android.compose.weather.util.Dimension1
//
//@Composable
//fun <T> SingleLineChart(
//    modifier: Modifier = Modifier,
//    chartData: List<T>,
//    lineValues: (T) -> Float,
//    verticalAxisValues: List<Float>,
//    horizontalAxisLabelColor: Color = DefaultAxisLabelColor,
//    horizontalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
//    verticalAxisLabelColor: Color = DefaultAxisLabelColor,
//    verticalAxisLabelFontSize: TextUnit = DefaultAxisLabelFontSize,
//    axisColor: Color = Color.Gray,
//    strokeWidth: Dp = DefaultStrokeWidth,
//    strokeColor: Color,
//    dotColor: Color
//) {
//    val chartHeight = HorizontalLineSpacing * verticalAxisValues.size
//
//    Canvas(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(chartHeight)
//    ) {
//        val bottomAreaHeight = horizontalAxisLabelFontSize.toPx()
//        val verticalAxisLength = size.height - bottomAreaHeight
//
//        println("----------------")
//
//        val leftAreaWidth =
//            (verticalAxisValues[verticalAxisValues.size - 1].toString().length * verticalAxisLabelFontSize.toPx()
//                .div(1.75)).toInt()
//        val horizontalAxisLength = size.width - leftAreaWidth
//
//        val distanceBetweenVerticalAxisValues = (verticalAxisLength / (verticalAxisValues.size - 1))
//
//        val axisThicknessPx = 1.dp.toPx()
//
//        //draw horizontal axis
//        drawRect(
//            color = axisColor,
//            topLeft = Offset(leftAreaWidth.toFloat(), verticalAxisLength),
//            size = Size(horizontalAxisLength, axisThicknessPx)
//        )
//
//        //draw vertical axis
//        drawRect(
//            color = axisColor,
//            topLeft = Offset(leftAreaWidth.toFloat(), 0.0f),
//            size = Size(axisThicknessPx, verticalAxisLength)
//        )
//
//        //draw horizontal lines & labels
//        verticalAxisValues.forEachIndexed { index, fl ->
//            val x = leftAreaWidth / 2.toFloat()
//            val y = verticalAxisLength - (distanceBetweenVerticalAxisValues).times(index)
//
//            println("-------lineY: $y")
//
//            drawContext.canvas.nativeCanvas.run {
//                drawText(
//                    fl.toString(),
//                    x,
//                    y + verticalAxisLabelFontSize.toPx() / 2,
//                    Paint().apply {
//                        textSize = verticalAxisLabelFontSize.toPx()
//                        color = verticalAxisLabelColor.toArgb()
//                        textAlign = Paint.Align.CENTER
//                    }
//                )
//            }
//
//            drawRect(
//                color = axisColor,
//                topLeft = Offset(leftAreaWidth.toFloat(), y),
//                size = Size(horizontalAxisLength, axisThicknessPx)
//            )
//        }
//
//        //draw line values
//        val barWidth = (drawContext.size.width - leftAreaWidth) / chartData.size
//        val minValue = verticalAxisValues.minOf { it }
//        val deltaRange = verticalAxisValues.maxOf { it } - verticalAxisValues.minOf { it }
//
//        var previousOffset: Offset? = null
//
//        val circleOffsets = mutableListOf<Offset>()
//
//        chartData.forEachIndexed { index, t ->
//            var x = barWidth * index
//            x += leftAreaWidth
//
//            val deltaValue = lineValues(t) - minValue
//            val valuePercent = deltaValue / deltaRange
//
//
//            val barHeightInPixel = valuePercent * verticalAxisLength
//            val y = verticalAxisLength - barHeightInPixel
//
//            println("-------line value: ${lineValues(t)}")
//            println("-------verticalAxisLength: $verticalAxisLength")
//            println("-------valuePercent: $valuePercent")
//            println("-------barHeightInPixel: $barHeightInPixel")
//            println("-------y: $y")
//            println("----------------")
//
//            val currentOffset = Offset(x, y)
//
//            val end = Offset((currentOffset.x + barWidth.div(2)), currentOffset.y)
//
//            circleOffsets.add(end)
//
//            previousOffset?.let {
//                val start = Offset(it.x + barWidth.div(2), it.y)
//                drawLine(
//                    start = start,
//                    end = end,
//                    color = strokeColor,
//                    strokeWidth = strokeWidth.toPx()
//                )
//            }
//
//            previousOffset = currentOffset
//
//            drawContext.canvas.nativeCanvas.apply {
//                drawText(
//                    "test",
//                    (currentOffset.x + barWidth.div(2)),
//                    verticalAxisLength + horizontalAxisLabelFontSize.toPx(),
//                    Paint().apply {
//                        textSize = horizontalAxisLabelFontSize.toPx()
//                        color = horizontalAxisLabelColor.toArgb()
//                        textAlign = Paint.Align.CENTER
//                    }
//                )
//            }
//        }
//
//        circleOffsets.forEach {
//            drawCircle(
//                color = dotColor,
//                center = it,
//                radius = strokeWidth.times(1.5f).toPx()
//            )
//        }
//    }
//}
//
//
//val DefaultStrokeWidth = Dimension1
//val DefaultAxisLabelColor = Color.Gray
//val DefaultAxisLabelFontSize = 13.sp
//val DefaultAxisThickness = 1.dp
//val DefaultContentPadding = 4.dp
//
//val HorizontalLineSpacing = 30.dp