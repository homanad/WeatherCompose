package com.homalab.android.compose.benchmark

import android.graphics.Point
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import com.homalab.android.compose.constants.GlobalConstants
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class GoToDetailBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private fun MacrobenchmarkScope.goToDetail() {
        val detailButton = device.findObject(By.desc(GlobalConstants.DetailButtonDescription))
        detailButton.click()
    }

    @Test
    fun dragFrontLayerUp() = benchmarkRule.measureRepeated(
        packageName = "com.homalab.android.compose.weather",
        metrics = listOf(FrameTimingMetric()),
        iterations = 3,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        }
    ) {
        goToDetail()

        val frontLayer = device.findObject(By.desc(GlobalConstants.DetailFrontLayerDescription))
        frontLayer.drag(Point(frontLayer.visibleCenter.x, frontLayer.visibleBounds.top - 1000))
    }

    @Test
    fun selectDay() = benchmarkRule.measureRepeated(
        packageName = "com.homalab.android.compose.weather",
        metrics = listOf(FrameTimingMetric()),
        iterations = 3,
        startupMode = StartupMode.COLD,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        }
    ) {
        goToDetail()

        val dayTabs = device.findObject(By.desc(GlobalConstants.DayTabsDescription))
        dayTabs.children[Random.nextInt(5)].click()
    }
}