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

import com.example.core.Resource
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import com.example.currency.repo.CurrencyRepository
import com.example.currency.sources.LocalDataSource
import com.example.currency.sources.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CurrencyRepository {

    override fun getExchangeRates(): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading())

        try {
            val response = remoteDataSource.fetchExchangeRates()
            val ratesList = response.rates.map { (currency, rate) ->
                CurrencyRate(currency, rate)
            }

            localDataSource.saveExchangeRates(ratesList)
            emit(Resource.Success(ratesList))
        } catch (e: Exception) {
            val cachedRates = localDataSource.getExchangeRates().firstOrNull()
            if (cachedRates.isNullOrEmpty()) {
                emit(Resource.Error("Failed to fetch exchange rates: ${e.message}"))
            } else {
                emit(Resource.Success(cachedRates))
            }
        }
    }

    override fun getCurrencySymbols(): Flow<Resource<List<CurrencySymbol>>> = flow {
        emit(Resource.Loading())

        try {
            val cachedSymbols = localDataSource.getCurrencySymbols().firstOrNull()
            if (!cachedSymbols.isNullOrEmpty()) {
                emit(Resource.Success(cachedSymbols))
            }

            val response = remoteDataSource.fetchCurrencySymbols()
            val symbols = response.symbols.map { (code, name) ->
                CurrencySymbol(code, name)
            }

            localDataSource.saveCurrencySymbols(symbols)
            emit(Resource.Success(symbols))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error fetching currency symbols"))
        }
    }

    override suspend fun convertCurrency(amount: Double, from: String, to: String): Double {
        val rates = localDataSource.getExchangeRates().firstOrNull()
            ?: throw IllegalStateException("No exchange rates available")

        val fromRate = rates.find { it.currency == from }?.rate
            ?: throw IllegalArgumentException("Exchange rate for $from not found")
        val toRate = rates.find { it.currency == to }?.rate
            ?: throw IllegalArgumentException("Exchange rate for $to not found")

        return (amount / fromRate) * toRate
    }

}
