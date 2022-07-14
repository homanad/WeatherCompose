package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayData
import com.homalab.android.compose.weather.presentation.mapper.ForecastDayItem
import com.homalab.android.compose.weather.presentation.mapper.toForecastDayData
import com.homalab.android.compose.weather.presentation.ui.vm.MainViewModel
import com.homalab.android.compose.weather.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun DetailScreen(
    lat: Double,
    lon: Double,
    detailState: DetailState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(lat, lon) {
        detailState.forecastDayData = mainViewModel.getForecastData(lat, lon)?.toForecastDayData()
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = Dimension1, bottom = Dimension1),
        ) {
            IconButton(
                modifier = Modifier
                    .padding(start = IconPadding)
                    .rotate(90f),
                onClick = onBackClick
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(
                modifier = Modifier.padding(start = Dimension1),
                text = detailState.forecastDayData?.city?.name ?: "",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        BackdropScaffold(
            modifier = modifier,
            scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
            frontLayerShape = BottomSheetShape,
            frontLayerScrimColor = Color.Unspecified,
            appBar = {
                DayTabs(
                    items = detailState.forecastDayData?.items?.map {
                        TimeFormatter.formatDetailDayTime(
                            it.dt,
                            detailState.forecastDayData?.city?.timeZone ?: 0
                        )
                    } ?: listOf(),
                    selectedTab = detailState.selectedTab,
                    onTabSelected = { detailState.selectedTab = it }
                )
            },
            backLayerContent = {
                DetailBackLayerDisplay(
                    forecastDayItem = detailState.forecastDayItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState())
                )
            },
            backLayerBackgroundColor = Color.Transparent,
            frontLayerContent = {
                AnimatedContent(
                    targetState = detailState.selectedTab,
                    transitionSpec = {
                        val direction = if (initialState < targetState)
                            AnimatedContentScope.SlideDirection.Left else AnimatedContentScope.SlideDirection.Right

                        slideIntoContainer(
                            towards = direction,
                            animationSpec = tween(DURATION_LONG)
                        ) with slideOutOfContainer(
                            towards = direction,
                            animationSpec = tween(DURATION_LONG)
                        ) using SizeTransform(
                            sizeAnimationSpec = { _, _ ->
                                tween(DURATION_LONG, easing = EaseInOut)
                            }
                        )
                    }
                ) {
                    DetailFrontLayerDisplay(
                        forecastDayItem = detailState.forecastDayItem,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            },
            stickyFrontLayer = true
        )
    }
}

@Stable
class DetailState(
    forecastDayData: ForecastDayData?,
    selectedTab: Int = 0
) {
    var forecastDayData by mutableStateOf(forecastDayData)
    var selectedTab by mutableStateOf(selectedTab)

    val forecastDayItem: ForecastDayItem?
        get() = forecastDayData?.items?.get(selectedTab)
}

@Composable
fun rememberDetailState(
    forecastDayData: ForecastDayData? = null,
    selectedTab: Int = 0
) = remember {
    DetailState(forecastDayData, selectedTab)
}

val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = Dimension0,
    bottomEnd = Dimension0
)