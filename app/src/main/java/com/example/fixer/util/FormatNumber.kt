package com.example.fixer.util

import java.text.NumberFormat
import java.util.Locale


fun formatNumber(value: String): String {
    if (value.isEmpty()) return ""

    return try {
        val parsed = value.toDoubleOrNull()
        if (parsed != null) {
            NumberFormat.getNumberInstance(Locale.getDefault()).format(parsed)
        } else {
            value
        }
    } catch (e: NumberFormatException) {
        value
    }
}