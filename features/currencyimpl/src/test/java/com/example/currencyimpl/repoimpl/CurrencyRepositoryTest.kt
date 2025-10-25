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
import com.kanake.core.Resource
import com.kanake.core.model.CurrencyRate
import com.kanake.currency.sources.LocalDataSource
import com.kanake.currency.sources.RemoteDataSource
import com.kanake.network.NetworkChecker
import com.kanake.testing.utils.currencyRates
import com.kanake.testing.utils.currencySymbols
import com.kanake.testing.utils.exchangeRatesResponse
import com.kanake.testing.utils.symbolsResponse
import com.kanake.testing.utils.testExchangeRates
import com.kanake.currencyimpl.repoimpl.CurrencyRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyRepositoryTest {

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var networkChecker: NetworkChecker

    private lateinit var repository: CurrencyRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CurrencyRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            networkChecker = networkChecker
        )
    }

    @Test
    fun `getExchangeRates should emit success when API call succeeds`() = runTest {
        every { networkChecker.isInternetAvailable() } returns true
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
    fun `getExchangeRates should emit cached data when offline`() = runTest {
        every { networkChecker.isInternetAvailable() } returns false
        every { localDataSource.getExchangeRates() } returns flowOf(currencyRates())

        repository.getExchangeRates().test {
            assert(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assert(success.data == currencyRates())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getExchangeRates should emit error when API and cache both fail`() = runTest {
        every { networkChecker.isInternetAvailable() } returns false
        every { localDataSource.getExchangeRates() } returns flowOf(emptyList())

        repository.getExchangeRates().test {
            assert(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assert(error.message == "No internet connection.")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getExchangeRates should emit error when API call fails and cache empty`() = runTest {
        every { networkChecker.isInternetAvailable() } returns true
        coEvery { remoteDataSource.fetchExchangeRates() } throws IOException("Server down")
        every { localDataSource.getExchangeRates() } returns flowOf(emptyList())

        repository.getExchangeRates().test {
            assert(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assert(error.message == "Server down" || error.message == "Failed to fetch exchange rates")
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `getCurrencySymbols should emit success when API call succeeds`() = runTest {
        every { networkChecker.isInternetAvailable() } returns true
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
    fun `getCurrencySymbols should emit cached data when offline`() = runTest {
        every { networkChecker.isInternetAvailable() } returns false
        every { localDataSource.getCurrencySymbols() } returns flowOf(currencySymbols())

        repository.getCurrencySymbols().test {
            assert(awaitItem() is Resource.Loading)
            val success = awaitItem() as Resource.Success
            assert(success.data == currencySymbols())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCurrencySymbols should emit error when API and cache both fail`() = runTest {
        every { networkChecker.isInternetAvailable() } returns false
        every { localDataSource.getCurrencySymbols() } returns flowOf(emptyList())

        repository.getCurrencySymbols().test {
            assert(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assert(error.message == "No internet connection.")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCurrencySymbols should emit error when API call fails and cache empty`() = runTest {
        every { networkChecker.isInternetAvailable() } returns true
        coEvery { remoteDataSource.fetchCurrencySymbols() } throws IOException("Network issue")
        every { localDataSource.getCurrencySymbols() } returns flowOf(emptyList())

        repository.getCurrencySymbols().test {
            assert(awaitItem() is Resource.Loading)
            val error = awaitItem() as Resource.Error
            assert(error.message == "Network issue" || error.message == "Failed to fetch currency symbols")
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `convertCurrency should correctly convert between different currencies`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(testExchangeRates())

        val convertedAmount = repository.convertCurrency(200.0, "GBP", "USD")

        assert(convertedAmount == (200.0 / 0.85) * 1.1)
    }

    @Test(expected = IllegalStateException::class)
    fun `convertCurrency should throw error if no exchange rates available and offline`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(emptyList())
        every { networkChecker.isInternetAvailable() } returns false

        repository.convertCurrency(100.0, "EUR", "USD")
    }

    @Test(expected = IllegalStateException::class)
    fun `convertCurrency should throw error if exchange rates are empty`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(emptyList())
        every { networkChecker.isInternetAvailable() } returns true

        repository.convertCurrency(100.0, "EUR", "USD")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertCurrency should throw error if from currency is not found`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(
            listOf(CurrencyRate("USD", 1.2))
        )

        repository.convertCurrency(100.0, "EUR", "USD")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convertCurrency should throw error if to currency is not found`() = runTest {
        every { localDataSource.getExchangeRates() } returns flowOf(
            listOf(CurrencyRate("EUR", 1.0))
        )

        repository.convertCurrency(100.0, "EUR", "USD")
    }
}
