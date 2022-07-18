package com.homalab.android.compose.weather.presentation.components.charts.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun Dp.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toDp() = LocalDensity.current.run { this@toDp.toDp() }