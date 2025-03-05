package com.example.currency.sources

import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse

interface RemoteDataSource {
    suspend fun fetchExchangeRates(): ExchangeRatesResponse
    suspend fun fetchCurrencySymbols(): SymbolsResponse
}