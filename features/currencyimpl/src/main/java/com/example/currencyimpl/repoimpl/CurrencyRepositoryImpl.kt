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
            val cachedRates = localDataSource.getExchangeRates().firstOrNull()
            if (!cachedRates.isNullOrEmpty()) {
                emit(Resource.Success(cachedRates))
            }

            val response = remoteDataSource.fetchExchangeRates()
            val rates = response.rates.map { (currency, rate) ->
                CurrencyRate(currency, rate)
            }

            localDataSource.saveExchangeRates(rates)  // Cache new rates
            emit(Resource.Success(rates))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error fetching exchange rates"))
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

            localDataSource.saveCurrencySymbols(symbols)  // Cache new symbols
            emit(Resource.Success(symbols))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error fetching currency symbols"))
        }
    }

    override suspend fun convertCurrency(amount: Double, from: String, to: String): Double {
        val rates = localDataSource.getExchangeRates().firstOrNull()
            ?: throw Exception("No exchange rates available")

        val fromRate = rates.find { it.currency == from }?.rate
        val toRate = rates.find { it.currency == to }?.rate

        if (fromRate == null || toRate == null) {
            throw Exception("Invalid currency codes")
        }

        return (amount / fromRate) * toRate
    }
}
