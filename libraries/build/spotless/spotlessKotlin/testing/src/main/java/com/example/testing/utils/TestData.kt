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