package com.done.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.done.app.data.repository.TipsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TipsUiState(
    val tip: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class TipsViewModel(
    private val repository: TipsRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(TipsUiState())
    val uiState: StateFlow<TipsUiState> =
        _uiState.asStateFlow()

    init {
        refreshTip()
    }

    fun refreshTip() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

            runCatching {
                repository.loadTip()
            }.onSuccess { tip ->
                _uiState.value =
                    TipsUiState(
                        tip = tip
                    )
            }.onFailure {
                _uiState.value =
                    TipsUiState(
                        tip = _uiState.value.tip,
                        error = "Could not load a tip. Check your internet connection."
                    )
            }
        }
    }
}

class TipsViewModelFactory(
    private val repository: TipsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return TipsViewModel(
            repository = repository
        ) as T
    }
}
