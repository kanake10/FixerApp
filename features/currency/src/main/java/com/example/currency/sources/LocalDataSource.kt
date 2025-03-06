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
package com.example.currency.sources

import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getExchangeRates(): Flow<List<CurrencyRate>>
    suspend fun saveExchangeRates(rates: List<CurrencyRate>)
    fun getCurrencySymbols(): Flow<List<CurrencySymbol>>
    suspend fun saveCurrencySymbols(symbols: List<CurrencySymbol>)
}
