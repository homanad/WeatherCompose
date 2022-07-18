package com.homalab.android.compose.weather.presentation.components.charts.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.homalab.android.compose.weather.presentation.components.charts.entities.LineEntity
import kotlinx.coroutines.delay

@Composable
fun AnimatedLine(
    modifier: Modifier,
    durationMillis: Int,
    delayMillis: Int,
    lineEntity: LineEntity
) {
    val animatable = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = animatable, block = {
        delay(delayMillis.toLong())
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
        )
    })

    Canvas(modifier = modifier, onDraw = {
        drawLine(
            color = lineEntity.color,
            start = lineEntity.start,
            end = Offset(
                (lineEntity.end.x * animatable.value) + lineEntity.start.x * (1f - animatable.value),
                (lineEntity.end.y * animatable.value) + lineEntity.start.y * (1f - animatable.value)
            ),
            strokeWidth = lineEntity.strokeWidth.toPx()
        )
    })
}