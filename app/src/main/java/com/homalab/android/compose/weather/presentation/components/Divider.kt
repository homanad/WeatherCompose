package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    lineHeight: Dp = DefaultHorizontalDividerHeight,
    color: Color = DefaultDividerColor
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(lineHeight)
            .background(color = color)
    )
}

val DefaultHorizontalDividerHeight = 1.dp
val DefaultDividerColor = Color.Gray