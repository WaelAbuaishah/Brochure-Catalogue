package com.android.brochurecatalogue.brochureslist

import android.content.res.Configuration
import androidx.annotation.OpenForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.data.model.domain.Brochure
import com.android.data.utils.Result
import com.android.data.utils.Source
import com.android.domain.usecase.BrochureFilter
import com.android.domain.usecase.GetBrochuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrochureViewModel @Inject constructor(
    private val getBrochuresUseCase: GetBrochuresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrochureUiState())
    val uiState: StateFlow<BrochureUiState> = _uiState.asStateFlow()

    init {
        loadBrochures()
    }

    @OpenForTesting
    fun loadBrochures(filter: BrochureFilter = BrochureFilter()) {
        viewModelScope.launch {

            getBrochuresUseCase.execute(GetBrochuresUseCase.Param(filter)).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        val source = result.source
                        _uiState.update { it.copy(isLoading = true, loadingSource = source) }
                    }

                    is Result.Success -> {
                        val source = result.source
                        _uiState.update {
                            it.copy(
                                brochures = result.data,
                                isLoading = false,
                                loadingSource = source
                            )
                        }
                    }

                    is Result.Failure -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.exception.message ?: "Unknown error occurred") }
                    }
                }
            }
        }
    }

    fun updateFilter(enableProximity: Boolean, maxDistance: Double) {
        _uiState.update { it.copy(filter = it.filter.copy(enableProximityFilter = enableProximity, maxDistance = maxDistance)) }
        loadBrochures(_uiState.value.filter)
    }

    fun updateColumnsBasedOnOrientation(orientation: Int) {
        _uiState.update { it.copy(numberOfColumns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2) }
    }
}

data class BrochureUiState(
    val brochures: List<Brochure> = emptyList(),
    val isLoading: Boolean = false,
    val loadingSource: Source? = null,
    val errorMessage: String? = null,
    val filter: BrochureFilter = BrochureFilter(),
    val numberOfColumns: Int = 2
)
