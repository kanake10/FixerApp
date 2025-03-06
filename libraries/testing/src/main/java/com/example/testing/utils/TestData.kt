package com.example.testing.utils

import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse

fun testExchangeRates() = listOf(
    CurrencyRate("USD", 1.1),
    CurrencyRate("EUR", 1.0),
    CurrencyRate("GBP", 0.85)
)

fun testCurrencySymbols() = listOf(
    CurrencySymbol("USD", "United States Dollar"),
    CurrencySymbol("EUR", "Euro"),
    CurrencySymbol("GBP", "British Pound Sterling")
)


fun exchangeRatesResponse() = listOf(
    ExchangeRatesResponse(
        base = "EUR",
        rates = mapOf("USD" to 1.2, "PLN" to 4.5)
    )
)

fun currencyRates() = listOf(
    CurrencyRate("USD", 1.2),
    CurrencyRate("PLN", 4.5)
)

fun symbolsResponse() = SymbolsResponse(
    symbols = mapOf("USD" to "US Dollar", "PLN" to "Polish Złoty")
)

fun  currencySymbols() = listOf(
    CurrencySymbol("USD", "US Dollar"),
    CurrencySymbol("PLN", "Polish Złoty")
)