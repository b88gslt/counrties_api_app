package com.example.hm_third_count.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hm_third_count.di.NetworkModule
import com.example.hm_third_count.presentation.countries.CountriesScreen
import com.example.hm_third_count.presentation.countries.CountriesViewModel
import com.example.hm_third_count.presentation.detail.CountryDetailScreen
import com.example.hm_third_count.presentation.detail.CountryDetailViewModel

@Composable
fun CountriesNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "countries"
    ) {
        composable("countries") {
            val viewModel: CountriesViewModel = viewModel {
                CountriesViewModel(NetworkModule.countriesRepository)
            }
            val uiState by viewModel.uiState.collectAsState()
            
            CountriesScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onCountryClick = { countryCode ->
                    navController.navigate("detail/$countryCode")
                }
            )
        }
        
        composable("detail/{countryCode}") { backStackEntry ->
            val countryCode = backStackEntry.arguments?.getString("countryCode") ?: return@composable
            val viewModel: CountryDetailViewModel = viewModel {
                CountryDetailViewModel(NetworkModule.countriesRepository, countryCode)
            }
            val uiState by viewModel.uiState.collectAsState()
            
            CountryDetailScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}