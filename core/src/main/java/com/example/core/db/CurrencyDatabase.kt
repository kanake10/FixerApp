package com.example.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.dao.CurrencyDao
import com.example.core.model.CurrencyRate
import com.example.core.model.CurrencySymbol

@Database(
    entities = [CurrencyRate::class, CurrencySymbol::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
