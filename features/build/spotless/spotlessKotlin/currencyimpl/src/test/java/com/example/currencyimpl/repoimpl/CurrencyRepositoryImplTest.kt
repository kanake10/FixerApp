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
package com.example.currencyimpl.repoimpl

import app.cash.turbine.test
import com.example.core.Resource
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse
import com.example.currency.sources.LocalDataSource
import com.example.currency.sources.RemoteDataSource
import com.example.testing.utils.currencyRates
import com.example.testing.utils.currencySymbols
import com.example.testing.utils.exchangeRatesResponse
import com.example.testing.utils.symbolsResponse
import com.example.testing.utils.testExchangeRates
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRepositoryTest {

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    @MockK
    lateinit var localDataSource: LocalDataSource

    private lateinit var repository: CurrencyRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CurrencyRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Test
    fun `getExchangeRates should emit success when API call succeeds`() = runTest {
        coEvery { remoteDataSource.fetchExchangeRates() } returns exchangeRatesResponse().first()
        coEvery { localDataSource.saveExchangeRates(any()) } just Runs
        every { localDataSource.getExchangeRates() } returns flowOf(currencyRates())

        repository.getExchangeRates().test {
            assert(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assert(success.data == currencyRates())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getExchangeRates should emit error when API call fails and no cache available`() = runTest {
        coEvery { remoteDataSource.fetchExchangeRates() } throws IOException("Network error")
        every { localDataSource.getExchangeRates() } returns flowOf(emptyList())

        repository.getExchangeRates().test {
            assert(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assert(error.message?.contains("Failed to fetch exchange rates") == true)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getExchangeRates should emit cached data when API call fails`() = runTest {
        coEvery { remoteDataSource.fetchExchangeRates() } throws IOException("Network error")
        every { localDataSource.getExchangeRates() } returns flowOf(currencyRates())

        repository.getExchangeRates().test {
            assert(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assert(success.data == currencyRates())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCurrencySymbols should emit success when API call succeeds`() = runTest {
        coEvery { remoteDataSource.fetchCurrencySymbols() } returns symbolsResponse()
        coEvery { localDataSource.saveCurrencySymbols(any()) } just Runs
        every { localDataSource.getCurrencySymbols() } returns flowOf(currencySymbols())

        repository.getCurrencySymbols().test {
            assert(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assert(success.data == currencySymbols())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCurrencySymbols should emit error when API call fails`() = runTest {
        coEvery { remoteDataSource.fetchCurrencySymbols() } throws IOException("Network error")
        every { localDataSource.getCurrencySymbols() } returns flowOf(emptyList())

        repository.getCurrencySymbols().test {
            assert(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assert(error.message == "Network error")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCurrencySymbols should emit cached data when API call fails`() = runTest {
        coEvery { remoteDataSource.fetchCurrencySymbols() } throws IOException("Network error")
        every { localDataSource.getCurrencySymbols() } returns flowOf(currencySymbols())

        repository.getCurrencySymbols().test {
            assert(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assert(success.data == currencySymbols())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `convertCurrency should correctly convert between different currencies`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(testExchangeRates())

        val convertedAmount = repository.convertCurrency(200.0, "GBP", "USD")

        assert(convertedAmount == (200.0 / 0.85) * 1.1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertCurrency should throw error if from currency is not found`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(listOf(CurrencyRate("USD", 1.2)))

        repository.convertCurrency(100.0, "EUR", "USD")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertCurrency should throw error if to currency is not found`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(listOf(CurrencyRate("EUR", 1.0)))

        repository.convertCurrency(100.0, "EUR", "USD")
    }
}



