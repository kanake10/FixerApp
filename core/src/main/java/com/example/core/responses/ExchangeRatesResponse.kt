package com.example.core.responses

data class ExchangeRatesResponse(
    val base: String,
    val rates: Map<String, Double>
)
