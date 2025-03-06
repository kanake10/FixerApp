package com.example.fixer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Resource
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import com.example.currency.repo.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState

    init {
        fetchExchangeRates()
        fetchCurrencySymbols()
    }

    fun fetchExchangeRates() {
        viewModelScope.launch {
            repository.getExchangeRates().collect { result ->
                _uiState.value = when (result) {
                    is Resource.Success -> _uiState.value.copy(exchangeRates = result.data, isLoading = false)
                    is Resource.Error -> _uiState.value.copy(error = result.message, isLoading = false)
                    is Resource.Loading -> _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun fetchCurrencySymbols() {
        viewModelScope.launch {
            repository.getCurrencySymbols().collect { result ->
                _uiState.value = when (result) {
                    is Resource.Success -> _uiState.value.copy(currencySymbols = result.data, isLoading = false)
                    is Resource.Error -> _uiState.value.copy(error = result.message, isLoading = false)
                    is Resource.Loading -> _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun convertCurrency(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val convertedAmount = repository.convertCurrency(amount, from, to)
                _uiState.value = _uiState.value.copy(convertedAmount = convertedAmount, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun setError(message: String?) {
        _uiState.value = _uiState.value.copy(error = message)
    }


}

data class CurrencyUiState(
    val isLoading: Boolean = false,
    val exchangeRates: List<CurrencyRate>? = null,
    val currencySymbols: List<CurrencySymbol>? = null,
    val error: String? = null,
    val convertedAmount: Double = 0.0
)