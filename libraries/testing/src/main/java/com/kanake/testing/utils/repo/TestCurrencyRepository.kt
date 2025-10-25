/*
 * Copyright 2025 Ezra Kanake.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanake.testing.utils.repo

import com.kanake.core.Resource
import com.kanake.core.model.CurrencyRate
import com.kanake.core.model.CurrencySymbol
import com.kanake.currency.repo.CurrencyRepository
import com.kanake.testing.utils.testCurrencySymbols
import com.kanake.testing.utils.testExchangeRates
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
