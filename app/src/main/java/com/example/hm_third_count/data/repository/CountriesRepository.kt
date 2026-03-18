package com.example.hm_third_count.data.repository

import com.example.hm_third_count.data.api.CountriesApi
import com.example.hm_third_count.data.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountriesRepository(private val api: CountriesApi) {
    
    companion object {
        private val _favoritesState = MutableStateFlow<Set<String>>(emptySet())
    }
    
    val favorites: Flow<Set<String>> = _favoritesState.asStateFlow()
    
    suspend fun getAllCountries(): Result<List<Country>> {
        return try {
            val countries = api.getAllCountries()
            Result.success(countries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchCountries(query: String): Result<List<Country>> {
        return try {
            val countries = if (query.isBlank()) {
                api.getAllCountries()
            } else {
                api.searchCountriesByName(query)
            }
            Result.success(countries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCountryByCode(code: String): Result<Country?> {
        return try {
            val countries = api.getCountryByCode(code)
            Result.success(countries.firstOrNull())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCountriesByRegion(region: String): Result<List<Country>> {
        return try {
            val countries = api.getCountriesByRegion(region)
            Result.success(countries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun addToFavorites(countryCode: String) {
        _favoritesState.value = _favoritesState.value + countryCode
    }
    
    fun removeFromFavorites(countryCode: String) {
        _favoritesState.value = _favoritesState.value - countryCode
    }
    
    fun isFavorite(countryCode: String): Boolean {
        return _favoritesState.value.contains(countryCode)
    }
}