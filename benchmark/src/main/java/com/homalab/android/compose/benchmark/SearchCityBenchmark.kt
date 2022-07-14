package com.homalab.android.compose.benchmark

import android.graphics.Point
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import com.homalab.android.compose.constants.GlobalConstants
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class SearchCityBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun searchCity() = benchmarkRule.measureFrameTiming {
        val searchBarInput = device.findObject(By.desc(GlobalConstants.SearchBarInputDescription))
        searchBarInput.click()

        searchBarInput.text = "Ho Chi Minh"

        val searchResult = device.findObject(By.desc(GlobalConstants.CityListDescription))
        Thread.sleep(200)
        searchResult.children[0].click()
    }

    @Test
    fun selectSavedCity() = benchmarkRule.measureFrameTiming {
        val bottomSheetLayout = device.findObject(By.desc(GlobalConstants.BottomSheetDescription))

        val bounds = bottomSheetLayout.visibleBounds
        bottomSheetLayout.drag(Point(bottomSheetLayout.visibleCenter.x, bounds.top - 1000))

        val cityList = device.findObject(By.desc(GlobalConstants.CityListDescription))
        Thread.sleep(200)
        cityList.children[Random.nextInt(5)].click()
    }
}