package com.example.hm_third_count.presentation.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hm_third_count.data.repository.CountriesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CountriesViewModel(
    private val repository: CountriesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CountriesUiState())
    val uiState: StateFlow<CountriesUiState> = _uiState.asStateFlow()
    
    private var searchJob: Job? = null
    
    init {
        loadCountries()
        observeFavorites()
    }
    
    fun onEvent(event: CountriesEvent) {
        when (event) {
            is CountriesEvent.SearchQueryChanged -> {
                _uiState.value = _uiState.value.copy(
                    searchQuery = event.query,
                    selectedRegion = "",
                    showFavoritesOnly = false
                )
                searchCountries(event.query)
            }
            is CountriesEvent.RegionSelected -> {
                _uiState.value = _uiState.value.copy(
                    selectedRegion = event.region,
                    searchQuery = "",
                    showFavoritesOnly = false
                )
                filterByRegion(event.region)
            }
            CountriesEvent.ShowFavorites -> {
                _uiState.value = _uiState.value.copy(
                    showFavoritesOnly = true,
                    selectedRegion = "",
                    searchQuery = ""
                )
                showFavoriteCountries()
            }
            is CountriesEvent.ToggleFavorite -> {
                toggleFavorite(event.countryCode)
            }
            CountriesEvent.Retry -> {
                loadCountries()
            }
            CountriesEvent.LoadCountries -> {
                loadCountries()
            }
        }
    }
    
    private fun loadCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                searchQuery = "",
                selectedRegion = "",
                showFavoritesOnly = false
            )
            
            repository.getAllCountries()
                .onSuccess { countries ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        countries = countries,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }
    
    private fun searchCountries(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.searchCountries(query)
                .onSuccess { countries ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        countries = countries,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Search failed"
                    )
                }
        }
    }
    
    private fun filterByRegion(region: String) {
        if (region.isEmpty()) {
            loadCountries()
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getCountriesByRegion(region)
                .onSuccess { countries ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        countries = countries,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Filter failed"
                    )
                }
        }
    }
    
    private fun showFavoriteCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getAllCountries()
                .onSuccess { allCountries ->
                    val favoriteCountries = allCountries.filter { country ->
                        _uiState.value.favorites.contains(country.code)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        countries = favoriteCountries,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load favorites"
                    )
                }
        }
    }
    
    private fun toggleFavorite(countryCode: String) {
        if (repository.isFavorite(countryCode)) {
            repository.removeFromFavorites(countryCode)
        } else {
            repository.addToFavorites(countryCode)
        }
        
        if (_uiState.value.showFavoritesOnly) {
            showFavoriteCountries()
        }
    }
    
    private fun observeFavorites() {
        repository.favorites
            .onEach { favorites ->
                _uiState.value = _uiState.value.copy(favorites = favorites)
            }
            .launchIn(viewModelScope)
    }
}