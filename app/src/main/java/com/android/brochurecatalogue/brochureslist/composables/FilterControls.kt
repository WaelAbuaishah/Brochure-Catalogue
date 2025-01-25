package com.android.brochurecatalogue.brochureslist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.domain.usecase.BrochureFilter
import java.util.Locale

@Composable
fun FilterControls(
    filter: BrochureFilter,
    onFilterChange: (Boolean, Double) -> Unit
) {
    var isEnabled by remember { mutableStateOf(filter.enableProximityFilter) }
    var distance by remember { mutableDoubleStateOf(filter.maxDistance) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show Only Nearby Brochures",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Switch(
                    checked = isEnabled,
                    onCheckedChange = {
                        isEnabled = it
                        onFilterChange(isEnabled, distance)
                    }
                )
            }


            if (isEnabled) {
                Text(
                    text = "Distance Range: (${String.format(Locale.getDefault(),"%.1f", distance)} km)",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Slider(
                    value = distance.toFloat(),
                    onValueChange = { newValue ->
                        distance = newValue.toDouble()
                        onFilterChange(isEnabled, distance)
                    },
                    valueRange = 1f..20f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Filter Enabled")
@Composable
fun PreviewFilterControlsEnabled() {
    val sampleFilter = BrochureFilter(
        enableProximityFilter = true,
        maxDistance = 15.0
    )

    FilterControls(
        filter = sampleFilter,
        onFilterChange = { isEnabled, distance -> }
    )
}

@Preview(showBackground = true, name = "Filter Disabled")
@Composable
fun PreviewFilterControlsDisabled() {
    val sampleFilter = BrochureFilter(
        enableProximityFilter = false,
        maxDistance = 0.0
    )

    FilterControls(
        filter = sampleFilter,
        onFilterChange = { isEnabled, distance -> }
    )
}
