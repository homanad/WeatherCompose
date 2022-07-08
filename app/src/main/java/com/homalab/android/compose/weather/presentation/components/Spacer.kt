package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.Dimension4

@Composable
fun SmallSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(Dimension1))
}

@Composable
fun DefaultSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(Dimension2))
}

@Composable
fun LargeSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(Dimension4))
}