package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.Dimension4

@Composable
fun SmallVerticalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(Dimension1))
}

@Composable
fun DefaultVerticalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(Dimension2))
}

@Composable
fun LargeVerticalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(Dimension4))
}

@Composable
fun SmallHorizontalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(Dimension1))
}

@Composable
fun DefaultHorizontalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(Dimension2))
}

@Composable
fun LargeHorizontalSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(Dimension4))
}