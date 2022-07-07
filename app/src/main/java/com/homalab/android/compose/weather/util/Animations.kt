package com.homalab.android.compose.weather.util

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@OptIn(ExperimentalAnimationApi::class)
fun getDisplayEnterTransition(): EnterTransition {
    return scaleIn() + fadeIn() + slideInVertically(
        animationSpec = tween(400),
        initialOffsetY = { fullHeight -> fullHeight })
}

fun getDisplayExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(200)
    )
}