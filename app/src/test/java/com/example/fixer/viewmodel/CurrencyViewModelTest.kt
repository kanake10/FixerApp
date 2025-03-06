package com.example.fixer.viewmodel

import com.example.core.Resource
import com.example.testing.utils.base.BaseViewModelTest
import com.example.testing.utils.repo.TestCurrencyRepository
import com.example.testing.utils.testCurrencySymbols
import com.example.testing.utils.testExchangeRates
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: CurrencyViewModel
    private val repository = TestCurrencyRepository()

    @Before
    fun setUp() {
        viewModel = CurrencyViewModel(repository)
    }

    @Test
    fun `fetchExchangeRates should update uiState with exchange rates`() = runTest {
        viewModel.fetchExchangeRates()

        val state = viewModel.uiState.first()
        assertThat(state.exchangeRates).isEqualTo(testExchangeRates())
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
    }

    @Test
    fun `fetchExchangeRates should handle error state`() = runTest {
        repository.setExchangeRates(Resource.Error("Failed to fetch rates"))

        viewModel.fetchExchangeRates()

        val state = viewModel.uiState.first()
        assertThat(state.error).isEqualTo("Failed to fetch rates")
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `fetchCurrencySymbols should update uiState with currency symbols`() = runTest {
        viewModel.fetchCurrencySymbols()

        val state = viewModel.uiState.first()
        assertThat(state.currencySymbols).isEqualTo(testCurrencySymbols())
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `convertCurrency should update uiState with converted amount`() = runTest {
        val amount = 100.0
        val from = "EUR"
        val to = "USD"
        repository.setConversionResult(110.0)

        viewModel.convertCurrency(amount, from, to)

        val state = viewModel.uiState.first()
        assertThat(state.convertedAmount).isEqualTo(110.0)
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
    }

    @Test
    fun `convertCurrency should handle conversion failure`() = runTest {
        repository.setShouldThrowException(true)

        viewModel.convertCurrency(100.0, "EUR", "USD")

        val state = viewModel.uiState.first()
        assertThat(state.error).isEqualTo("Conversion failed")
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `fetchCurrencySymbols should handle error state`() = runTest {
        repository.setCurrencySymbols(Resource.Error("Failed to fetch symbols"))

        viewModel.fetchCurrencySymbols()

        val state = viewModel.uiState.first()
        assertThat(state.error).isEqualTo("Failed to fetch symbols")
        assertThat(state.isLoading).isFalse()
    }


    @Test
    fun `setError should update uiState with error message`() = runTest {
        viewModel.setError("Test Error")

        val state = viewModel.uiState.first()
        assertThat(state.error).isEqualTo("Test Error")
    }
}
