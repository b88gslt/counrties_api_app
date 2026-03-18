package com.example.hm_third_count.data.api

import com.example.hm_third_count.data.model.Country
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesApi {
    
    @GET("v3.1/all")
    suspend fun getAllCountries(): List<Country>
    
    @GET("v3.1/name/{name}")
    suspend fun searchCountriesByName(@Path("name") name: String): List<Country>
    
    @GET("v3.1/alpha/{code}")
    suspend fun getCountryByCode(@Path("code") code: String): List<Country>
    
    @GET("v3.1/region/{region}")
    suspend fun getCountriesByRegion(@Path("region") region: String): List<Country>
    
    companion object {
        const val BASE_URL = "https://restcountries.com/"
    }
}