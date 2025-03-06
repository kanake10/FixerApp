package com.example.testing.utils

import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol

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
