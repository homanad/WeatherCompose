package com.homalab.android.compose.weather.presentation.components.charts.entities

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class LineEntity(val color: Color, val strokeWidth: Dp, val start: Offset, val end: Offset)