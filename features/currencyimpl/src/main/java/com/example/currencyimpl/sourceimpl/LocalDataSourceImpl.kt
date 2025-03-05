package com.example.currencyimpl.sourceimpl

import com.example.core.dao.CurrencyDao
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import com.example.currency.sources.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val currencyDao: CurrencyDao
) : LocalDataSource {

    override fun getExchangeRates(): Flow<List<CurrencyRate>> = currencyDao.getAllRates()

    override suspend fun saveExchangeRates(rates: List<CurrencyRate>) {
        currencyDao.insertRates(rates)
    }

    override fun getCurrencySymbols(): Flow<List<CurrencySymbol>> = currencyDao.getAllSymbols()

    override suspend fun saveCurrencySymbols(symbols: List<CurrencySymbol>) {
        currencyDao.insertSymbols(symbols)
    }
}