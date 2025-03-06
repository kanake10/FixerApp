package com.example.core.dao

import com.example.core.base.BaseDbTest
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
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
