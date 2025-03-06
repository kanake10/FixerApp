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
package com.example.currencyimpl.sourceimpl

import com.example.core.responses.ExchangeRatesResponse
import com.example.core.responses.SymbolsResponse
import com.example.currency.sources.RemoteDataSource
import com.example.network.FixerApiService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: FixerApiService
) : RemoteDataSource {

    override suspend fun fetchExchangeRates(): ExchangeRatesResponse {
        return apiService.getExchangeRates()
    }

    override suspend fun fetchCurrencySymbols(): SymbolsResponse {
        return apiService.getCurrencySymbols()
    }
}