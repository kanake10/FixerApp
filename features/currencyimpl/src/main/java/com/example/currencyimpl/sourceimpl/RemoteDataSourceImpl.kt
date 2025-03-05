package com.example.currencyimpl.sourceimpl

import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse
import com.example.currency.sources.RemoteDataSource
import com.example.network.FixerApiService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: FixerApiService
) : RemoteDataSource {

    override suspend fun fetchExchangeRates(): ExchangeRatesResponse {
        return apiService.getExchangeRates()
    }

    override suspend fun fetchCurrencySymbols(): SymbolsResponse {
        return apiService.getCurrencySymbols()
    }
}