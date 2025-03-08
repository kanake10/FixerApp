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
import com.example.network.NetworkChecker
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.UnknownHostException

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val networkChecker: NetworkChecker
) : CurrencyRepository {

    override fun getExchangeRates(): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading())

        if (!networkChecker.isInternetAvailable()) {
            val cachedRates = localDataSource.getExchangeRates().firstOrNull()
            if (!cachedRates.isNullOrEmpty()) {
                emit(Resource.Success(cachedRates))
            } else {
                emit(Resource.Error("No internet connection."))
            }
            return@flow
        }
        try {
            val response = remoteDataSource.fetchExchangeRates()
            val ratesList = response.rates.map { (currency, rate) ->
                CurrencyRate(currency, rate)
            }

            localDataSource.saveExchangeRates(ratesList)
            emit(Resource.Success(ratesList))
        } catch (e: UnknownHostException) {
            emit(Resource.Error("No internet connection. Please check your network."))
        } catch (e: IOException) {
            emit(Resource.Error("Network error. Please try again."))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override fun getCurrencySymbols(): Flow<Resource<List<CurrencySymbol>>> = flow {
        emit(Resource.Loading())

        if (!networkChecker.isInternetAvailable()) {
            val cachedSymbols = localDataSource.getCurrencySymbols().firstOrNull()
            if (!cachedSymbols.isNullOrEmpty()) {
                emit(Resource.Success(cachedSymbols))
            } else {
                emit(Resource.Error("No internet connection."))
            }
            return@flow
        }
        try {
            val response = remoteDataSource.fetchCurrencySymbols()
            val symbols = response.symbols.map { (code, name) ->
                CurrencySymbol(code, name)
            }
            localDataSource.saveCurrencySymbols(symbols)
            emit(Resource.Success(symbols))
        } catch (e: UnknownHostException) {
            emit(Resource.Error("No internet connection. Please check your network."))
        } catch (e: IOException) {
            emit(Resource.Error("Network error. Please try again."))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error fetching currency symbols"))
        }
    }

    override suspend fun convertCurrency(amount: Double, from: String, to: String): Double {
        val rates = localDataSource.getExchangeRates().firstOrNull()
        if (rates.isNullOrEmpty()) {
            if (!networkChecker.isInternetAvailable()) {
                throw IllegalStateException("No internet connection. Please connect to proceed.")
            }
            throw IllegalStateException("Exchange rates are not available")
        }

        val fromRate = rates.find { it.currency == from }?.rate
        val toRate = rates.find { it.currency == to }?.rate

        if (fromRate == null) throw IllegalArgumentException("Exchange rate for $from not found")
        if (toRate == null) throw IllegalArgumentException("Exchange rate for $to not found")

        return (amount / fromRate) * toRate
    }
}


