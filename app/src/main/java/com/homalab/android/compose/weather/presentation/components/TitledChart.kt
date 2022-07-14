package com.homalab.android.compose.weather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2

@Composable
fun TitledChart(
    title: String,
    modifier: Modifier = Modifier,
    lineColors: LineColors,
    chartContent: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(end = Dimension1)
                    .weight(1f)
                    .background(lineColors.left),
                lineHeight = 2.dp
            )
            SmallVerticalSpacer()
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            SmallVerticalSpacer()
            HorizontalDivider(
                modifier = Modifier
                    .padding(start = Dimension1)
                    .weight(1f)
                    .background(lineColors.right),
                lineHeight = 2.dp
            )
        }
        chartContent.invoke()
    }
}

val titledChartModifier = Modifier
    .fillMaxWidth()
    .padding(start = Dimension2, end = Dimension2, bottom = Dimension2)

data class LineColors(val left: Color, val right: Color)