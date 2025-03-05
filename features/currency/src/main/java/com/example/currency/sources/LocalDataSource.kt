package com.example.currency.sources

import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getExchangeRates(): Flow<List<CurrencyRate>>
    suspend fun saveExchangeRates(rates: List<CurrencyRate>)
    fun getCurrencySymbols(): Flow<List<CurrencySymbol>>
    suspend fun saveCurrencySymbols(symbols: List<CurrencySymbol>)
}