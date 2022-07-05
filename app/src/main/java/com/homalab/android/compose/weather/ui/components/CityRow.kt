package com.homalab.android.compose.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.weather.domain.entity.City

@Composable
fun CityRow(city: City, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = city.name, modifier = Modifier.padding(Dimension3))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(SpacerLineSize)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}