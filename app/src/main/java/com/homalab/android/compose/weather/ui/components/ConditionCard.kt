package com.homalab.android.compose.weather.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.homalab.android.compose.weather.util.Dimension2
import com.homalab.android.compose.weather.util.Dimension4

@ExperimentalMaterial3Api
@Composable
fun ConditionCard(modifier: Modifier = Modifier, title: String, description: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Dimension4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(Dimension2))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}