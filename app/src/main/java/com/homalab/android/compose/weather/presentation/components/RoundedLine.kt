package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.RecentlyBottomSheetBarWidth

@Composable
fun RoundedLine(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .padding(Dimension1)
            .height(Dimension1)
            .width(RecentlyBottomSheetBarWidth)
            .clip(RoundedCornerShape(Dimension1))
            .background(Color.Gray)
    )
}