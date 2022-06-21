package com.homalab.android.compose.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.weather.ui.model.CityRecord

@Composable
fun CityRow(city: CityRecord, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(1.dp)
//                .background(Color.Gray)
//        )
        Text(text = city.name, modifier = Modifier.padding(12.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}