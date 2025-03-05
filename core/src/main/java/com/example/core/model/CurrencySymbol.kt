package com.example.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_symbols")
data class CurrencySymbol(
    @PrimaryKey val code: String,
    val name: String
)