package com.example.network

import com.example.core.Constants.GET_CURRENCY_SYMBOLS
import com.example.core.Constants.GET_EXCHANGE_RATES
import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse
import retrofit2.http.GET

interface FixerApiService {
    @GET(GET_EXCHANGE_RATES)
    suspend fun getExchangeRates(): ExchangeRatesResponse

    @GET(GET_CURRENCY_SYMBOLS)
    suspend fun getCurrencySymbols(): SymbolsResponse
}