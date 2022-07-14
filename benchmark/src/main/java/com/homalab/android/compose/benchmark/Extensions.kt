package com.homalab.android.compose.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule

fun MacrobenchmarkRule.measureFrameTiming(
    afterSetup: (MacrobenchmarkScope.() -> Unit)? = null,
    measureBlock: MacrobenchmarkScope.() -> Unit
) = measureRepeated(
    packageName = "com.homalab.android.compose.weather",
    metrics = listOf(FrameTimingMetric()),
    iterations = 3,
    startupMode = StartupMode.COLD,
    setupBlock = {
        pressHome()
        startActivityAndWait()
        afterSetup?.invoke(this)
    }
) {
    measureBlock.invoke(this)
}