package com.homalab.android.compose.weather.util

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@OptIn(ExperimentalAnimationApi::class)
fun getDisplayEnterTransition(): EnterTransition {
    return scaleIn() + fadeIn() + slideInVertically(
        animationSpec = tween(DURATION_MEDIUM),
        initialOffsetY = { fullHeight -> fullHeight })
}

fun getDisplayExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(DURATION_SMALL)
    )
}

fun getSlideUpEnterTransition(): EnterTransition {
    return slideInVertically(
        animationSpec = tween(DURATION_LONG),
        initialOffsetY = { fullHeight -> fullHeight })
}

fun getSlideDownEnterTransition(): EnterTransition {
    return slideInVertically(
        animationSpec = tween(DURATION_LONG),
        initialOffsetY = { fullHeight -> -fullHeight })
}

fun getSlideUpExitTransition(): ExitTransition {
    return slideOutVertically(
        animationSpec = tween(DURATION_LONG),
        targetOffsetY = { fullHeight -> -fullHeight })
}

fun getSlideDownExitTransition(): ExitTransition {
    return slideOutVertically(
        animationSpec = tween(DURATION_LONG),
        targetOffsetY = { fullHeight -> fullHeight })
}

const val DURATION_LONG = 600
private const val DURATION_MEDIUM = 400
private const val DURATION_SMALL = 200