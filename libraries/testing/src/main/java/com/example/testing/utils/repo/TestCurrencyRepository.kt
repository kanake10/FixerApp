package com.example.testing.utils.repo

import com.example.core.Resource
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import com.example.currency.repo.CurrencyRepository
import com.example.testing.utils.testCurrencySymbols
import com.example.testing.utils.testExchangeRates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestCurrencyRepository : CurrencyRepository {
    private val exchangeRatesFlow = MutableStateFlow<Resource<List<CurrencyRate>>>(
        Resource.Success(testExchangeRates())
    )

    private val currencySymbolsFlow = MutableStateFlow<Resource<List<CurrencySymbol>>>(
        Resource.Success(testCurrencySymbols())
    )

    private var conversionResult: Double = 1.0
    private var shouldThrowException = false

    override fun getExchangeRates(): Flow<Resource<List<CurrencyRate>>> {
        return exchangeRatesFlow
    }

    override fun getCurrencySymbols(): Flow<Resource<List<CurrencySymbol>>> {
        return currencySymbolsFlow
    }

    override suspend fun convertCurrency(amount: Double, from: String, to: String): Double {
        if (shouldThrowException) {
            throw Exception("Conversion failed")
        }
        return conversionResult
    }

    fun setExchangeRates(result: Resource<List<CurrencyRate>>) {
        exchangeRatesFlow.update { result }
    }

    fun setCurrencySymbols(result: Resource<List<CurrencySymbol>>) {
        currencySymbolsFlow.update { result }
    }

    fun setConversionResult(result: Double) {
        conversionResult = result
    }

    fun setShouldThrowException(value: Boolean) {
        shouldThrowException = value
    }
}
