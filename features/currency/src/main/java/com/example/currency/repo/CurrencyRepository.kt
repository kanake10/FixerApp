package com.example.currency.repo

import com.example.core.Resource
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getExchangeRates(): Flow<Resource<List<CurrencyRate>>>
    fun getCurrencySymbols(): Flow<Resource<List<CurrencySymbol>>>
    suspend fun convertCurrency(amount: Double, from: String, to: String): Double
}