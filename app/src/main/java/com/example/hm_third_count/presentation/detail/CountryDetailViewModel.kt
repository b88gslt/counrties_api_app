package com.example.hm_third_count.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hm_third_count.data.repository.CountriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CountryDetailViewModel(
    private val repository: CountriesRepository,
    private val countryCode: String
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CountryDetailUiState())
    val uiState: StateFlow<CountryDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadCountryDetail()
        observeFavorites()
    }
    
    fun onEvent(event: CountryDetailEvent) {
        when (event) {
            CountryDetailEvent.Retry -> {
                loadCountryDetail()
            }
            CountryDetailEvent.ToggleFavorite -> {
                toggleFavorite()
            }
        }
    }
    
    private fun loadCountryDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getCountryByCode(countryCode)
                .onSuccess { country ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        country = country,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load country details"
                    )
                }
        }
    }
    
    private fun toggleFavorite() {
        if (repository.isFavorite(countryCode)) {
            repository.removeFromFavorites(countryCode)
        } else {
            repository.addToFavorites(countryCode)
        }
    }
    
    private fun observeFavorites() {
        repository.favorites
            .onEach { favorites ->
                _uiState.value = _uiState.value.copy(
                    isFavorite = favorites.contains(countryCode)
                )
            }
            .launchIn(viewModelScope)
    }
}