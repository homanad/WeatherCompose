package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    lineHeight: Dp = DefaultHorizontalDividerHeight,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(lineHeight)
    )
}

val DefaultHorizontalDividerHeight = 1.dp