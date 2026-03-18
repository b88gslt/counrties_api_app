package com.example.hm_third_count.data.api

import com.example.hm_third_count.data.model.Country
import com.example.hm_third_count.data.model.CountryFlags
import com.example.hm_third_count.data.model.CountryName
import com.example.hm_third_count.data.model.Currency

class MockCountriesApi : CountriesApi {
    
    private val mockCountries = listOf(
        Country(
            name = CountryName(common = "Russia", official = "Russian Federation"),
            code = "RUS",
            capital = listOf("Moscow"),
            region = "Europe",
            subregion = "Eastern Europe",
            population = 144104080,
            area = 17098242.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/ru.png",
                svg = "https://flagcdn.com/ru.svg"
            ),
            languages = mapOf("rus" to "Russian"),
            currencies = mapOf("RUB" to Currency(name = "Russian ruble", symbol = "₽")),
            timezones = listOf("UTC+03:00", "UTC+04:00"),
            borders = listOf("AZE", "BLR", "CHN", "EST", "FIN")
        ),
        Country(
            name = CountryName(common = "Germany", official = "Federal Republic of Germany"),
            code = "DEU",
            capital = listOf("Berlin"),
            region = "Europe",
            subregion = "Western Europe",
            population = 83240525,
            area = 357114.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/de.png",
                svg = "https://flagcdn.com/de.svg"
            ),
            languages = mapOf("deu" to "German"),
            currencies = mapOf("EUR" to Currency(name = "Euro", symbol = "€")),
            timezones = listOf("UTC+01:00"),
            borders = listOf("AUT", "BEL", "CZE", "DNK", "FRA")
        ),
        Country(
            name = CountryName(common = "United States", official = "United States of America"),
            code = "USA",
            capital = listOf("Washington, D.C."),
            region = "Americas",
            subregion = "North America",
            population = 329484123,
            area = 9833517.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/us.png",
                svg = "https://flagcdn.com/us.svg"
            ),
            languages = mapOf("eng" to "English"),
            currencies = mapOf("USD" to Currency(name = "United States dollar", symbol = "$")),
            timezones = listOf("UTC-12:00", "UTC-11:00", "UTC-10:00", "UTC-09:00"),
            borders = listOf("CAN", "MEX")
        ),
        Country(
            name = CountryName(common = "Japan", official = "Japan"),
            code = "JPN",
            capital = listOf("Tokyo"),
            region = "Asia",
            subregion = "Eastern Asia",
            population = 125836021,
            area = 377930.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/jp.png",
                svg = "https://flagcdn.com/jp.svg"
            ),
            languages = mapOf("jpn" to "Japanese"),
            currencies = mapOf("JPY" to Currency(name = "Japanese yen", symbol = "¥")),
            timezones = listOf("UTC+09:00"),
            borders = null
        ),
        Country(
            name = CountryName(common = "Brazil", official = "Federative Republic of Brazil"),
            code = "BRA",
            capital = listOf("Brasília"),
            region = "Americas",
            subregion = "South America",
            population = 212559409,
            area = 8515767.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/br.png",
                svg = "https://flagcdn.com/br.svg"
            ),
            languages = mapOf("por" to "Portuguese"),
            currencies = mapOf("BRL" to Currency(name = "Brazilian real", symbol = "R$")),
            timezones = listOf("UTC-05:00", "UTC-04:00", "UTC-03:00"),
            borders = listOf("ARG", "BOL", "COL", "GUF", "GUY")
        ),
        Country(
            name = CountryName(common = "Australia", official = "Commonwealth of Australia"),
            code = "AUS",
            capital = listOf("Canberra"),
            region = "Oceania",
            subregion = "Australia and New Zealand",
            population = 25687041,
            area = 7692024.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/au.png",
                svg = "https://flagcdn.com/au.svg"
            ),
            languages = mapOf("eng" to "English"),
            currencies = mapOf("AUD" to Currency(name = "Australian dollar", symbol = "$")),
            timezones = listOf("UTC+05:00", "UTC+06:30", "UTC+07:00"),
            borders = null
        ),
        Country(
            name = CountryName(common = "South Africa", official = "Republic of South Africa"),
            code = "ZAF",
            capital = listOf("Cape Town", "Pretoria", "Bloemfontein"),
            region = "Africa",
            subregion = "Southern Africa",
            population = 59308690,
            area = 1221037.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/za.png",
                svg = "https://flagcdn.com/za.svg"
            ),
            languages = mapOf("afr" to "Afrikaans", "eng" to "English", "zul" to "Zulu"),
            currencies = mapOf("ZAR" to Currency(name = "South African rand", symbol = "R")),
            timezones = listOf("UTC+02:00"),
            borders = listOf("BWA", "LSO", "MOZ", "NAM", "SWZ", "ZWE")
        ),
        Country(
            name = CountryName(common = "France", official = "French Republic"),
            code = "FRA",
            capital = listOf("Paris"),
            region = "Europe",
            subregion = "Western Europe",
            population = 67391582,
            area = 643801.0,
            flags = CountryFlags(
                png = "https://flagcdn.com/w320/fr.png",
                svg = "https://flagcdn.com/fr.svg"
            ),
            languages = mapOf("fra" to "French"),
            currencies = mapOf("EUR" to Currency(name = "Euro", symbol = "€")),
            timezones = listOf("UTC-10:00", "UTC-09:30", "UTC-09:00"),
            borders = listOf("AND", "BEL", "DEU", "ITA", "LUX")
        )
    )
    
    override suspend fun getAllCountries(): List<Country> {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)
        return mockCountries
    }
    
    override suspend fun searchCountriesByName(name: String): List<Country> {
        kotlinx.coroutines.delay(500)
        return mockCountries.filter { 
            it.name.common.contains(name, ignoreCase = true) ||
            it.name.official.contains(name, ignoreCase = true)
        }
    }
    
    override suspend fun getCountryByCode(code: String): List<Country> {
        kotlinx.coroutines.delay(300)
        return mockCountries.filter { it.code.equals(code, ignoreCase = true) }
    }
    
    override suspend fun getCountriesByRegion(region: String): List<Country> {
        kotlinx.coroutines.delay(500)
        return mockCountries.filter { it.region.equals(region, ignoreCase = true) }
    }
}