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
import androidx.compose.ui.geometry.Size
import com.homalab.android.compose.weather.presentation.components.charts.entities.BarEntity
import kotlinx.coroutines.delay

@Composable
fun AnimatedBar(
    modifier: Modifier = Modifier,
    durationMillis: Int,
    delayMillis: Long,
    barEntity: BarEntity
) {
    val animatable = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = animatable, block = {
        delay(delayMillis)
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis, easing = LinearEasing)
        )
    })
    val rect = barEntity.rectF
    Canvas(modifier = modifier, onDraw = {
        val y = rect.bottom - (rect.bottom - rect.top) * animatable.value
        drawRect(
            color = barEntity.color,
            topLeft = Offset(x = rect.left, y = y),
            size = Size(rect.right - rect.left, rect.bottom - y)
        )
    })
}