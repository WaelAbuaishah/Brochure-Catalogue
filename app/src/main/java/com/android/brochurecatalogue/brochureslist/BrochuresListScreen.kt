package com.android.brochurecatalogue.brochureslist

//import com.android.data.model.domain.ContentType
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.android.brochurecatalogue.brochureslist.composables.BrochureItem
import com.android.brochurecatalogue.brochureslist.composables.FilterControls
import com.android.brochurecatalogue.brochureslist.composables.FullScreenLoading
import com.android.data.model.domain.Brochure
import com.android.data.utils.Source

@Composable
fun BrochuresListScreen(modifier: Modifier = Modifier, viewModel: BrochureViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    // TODO: Think again about uiState and the recomposition behaivour, maybe it worth it to do some changes on them
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration.orientation) {
        viewModel.updateColumnsBasedOnOrientation(configuration.orientation)
    }

    Column(modifier = modifier.fillMaxSize()) {

        FilterControls(
            filter = uiState.filter,
            onFilterChange = { enableProximity, maxDistance ->
                viewModel.updateFilter(enableProximity, maxDistance)
            }
        )

        if (uiState.isLoading && uiState.loadingSource == Source.LOCAL) {
            FullScreenLoading()
        } else {
            BrochureGrid(
                brochures = uiState.brochures,
                isRemoteLoading = uiState.isLoading && uiState.loadingSource == Source.REMOTE,
                numberOfCells = uiState.numberOfColumns
            )
        }

        uiState.errorMessage?.let { Text(text = it, color = Color.Red) }
    }
}

@Composable
fun BrochureGrid(brochures: List<Brochure>, isRemoteLoading: Boolean, numberOfCells: Int = 2) {
    LazyVerticalGrid(
        columns = Fixed(numberOfCells),
        modifier = Modifier.fillMaxSize()
    ) {
        if (isRemoteLoading) {

            item(span = { GridItemSpan(numberOfCells) }) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
        items(
            items = brochures,
            span = { brochure ->
                if (brochure is Brochure.PremiumBrochure) {
                    GridItemSpan(numberOfCells)
                } else {
                    GridItemSpan(1)
                }
            }
        ) { brochure ->
            BrochureItem(brochure = brochure)
        }
    }
}
