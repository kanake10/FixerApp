package com.example.network

import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApiService {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("access_key") apiKey: String = "818db86f8083d887073a235d2c0ed6c0"
    ): ExchangeRatesResponse

    @GET("symbols")
    suspend fun getCurrencySymbols(
        @Query("access_key") apiKey: String = "818db86f8083d887073a235d2c0ed6c0"
    ): SymbolsResponse
}