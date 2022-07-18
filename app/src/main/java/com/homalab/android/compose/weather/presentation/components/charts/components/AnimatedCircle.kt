package com.homalab.android.compose.weather.presentation.components.charts.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.homalab.android.compose.weather.presentation.components.charts.entities.CircleEntity
import kotlinx.coroutines.delay

@Composable
fun AnimatedCircle(
    modifier: Modifier,
    durationMillis: Int,
    delayMillis: Int,
    strokeWidth: Dp,
    circleEntity: CircleEntity
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
        drawCircle(
            color = circleEntity.color,
            center = circleEntity.offset,
            radius = strokeWidth.times(circleEntity.ratio).toPx() * animatable.value
        )
    })
}