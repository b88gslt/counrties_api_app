package com.example.hm_third_count.di

import com.example.hm_third_count.data.api.ApiCountriesApi
import com.example.hm_third_count.data.api.ApiCountriesApiAdapter
import com.example.hm_third_count.data.api.CountriesApi
import com.example.hm_third_count.data.repository.CountriesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NetworkModule {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    
    private val apiCountriesRetrofit = Retrofit.Builder()
        .baseUrl(ApiCountriesApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    
    private val apiCountriesApi = apiCountriesRetrofit.create(ApiCountriesApi::class.java)
    
    val countriesApi: CountriesApi = ApiCountriesApiAdapter(apiCountriesApi)
    
    val countriesRepository: CountriesRepository = CountriesRepository(countriesApi)
}