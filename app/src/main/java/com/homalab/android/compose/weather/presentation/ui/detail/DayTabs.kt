package com.homalab.android.compose.weather.presentation.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.weather.util.Dimension1
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.Dimension4

@Composable
fun DayTabs(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab,
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab])
                    .fillMaxSize()
                    .padding(Dimension1)
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(Dimension4))
            )
        },
        divider = {}
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedTab

            Tab(
                modifier = Modifier
                    .padding(Dimension1)
                    .clip(RoundedCornerShape(Dimension4)),
                selected = selected,
                onClick = { onTabSelected(index) }
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(vertical = Dimension2, horizontal = Dimension4),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}