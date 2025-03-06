package com.example.core.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRate>)

    @Query("SELECT * FROM currency_rates")
    fun getAllRates(): Flow<List<CurrencyRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(symbols: List<CurrencySymbol>)

    @Query("SELECT * FROM currency_symbols")
    fun getAllSymbols(): Flow<List<CurrencySymbol>>
}
