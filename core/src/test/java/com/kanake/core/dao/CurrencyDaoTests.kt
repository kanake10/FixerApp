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
package com.kanake.core.dao

import com.kanake.core.base.BaseDbTest
import com.kanake.core.model.CurrencyRate
import com.kanake.core.model.CurrencySymbol
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyDaoTest : BaseDbTest() {

    private lateinit var currencyDao: CurrencyDao

    @Before
    fun setupDao() {
        currencyDao = database.currencyDao()
    }

    @Test
    fun insertAndRetrieveRates() = runTest {
        currencyDao.insertRates(testExchangeRates)

        val collectedRates = currencyDao.getAllRates().first()

        Truth.assertThat(collectedRates).hasSize(testExchangeRates.size)
        Truth.assertThat(collectedRates).containsExactlyElementsIn(testExchangeRates)
    }

    @Test
    fun insertAndRetrieveSymbols() = runTest {
        currencyDao.insertSymbols(testCurrencySymbols)

        val collectedSymbols = currencyDao.getAllSymbols().first()

        Truth.assertThat(collectedSymbols).hasSize(testCurrencySymbols.size)
        Truth.assertThat(collectedSymbols).containsExactlyElementsIn(testCurrencySymbols)
    }

    @Test
    fun currencyDao_returns_empty_list_when_no_rates_inserted() = runTest {
        val retrievedRates = currencyDao.getAllRates().first()
        Truth.assertThat(retrievedRates).isEmpty()
    }

    @Test
    fun currencyDao_returns_empty_list_when_no_symbols_inserted() = runTest {
        val retrievedSymbols = currencyDao.getAllSymbols().first()
        Truth.assertThat(retrievedSymbols).isEmpty()
    }

    companion object {
        val testExchangeRates = listOf(
            CurrencyRate("USD", 1.1),
            CurrencyRate("EUR", 1.0),
            CurrencyRate("GBP", 0.85)
        )

        val testCurrencySymbols = listOf(
            CurrencySymbol("USD", "United States Dollar"),
            CurrencySymbol("EUR", "Euro"),
            CurrencySymbol("GBP", "British Pound Sterling")
        )
    }
}
