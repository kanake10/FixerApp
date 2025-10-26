package com.kanake.ratex

import com.kanake.ratex.util.formatNumber
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Locale

class NumberFormatTest {

    @Before
    fun setUp() {
        Locale.setDefault(Locale.getDefault())
    }

    @Test
    fun `formatNumber should format decimals correctly`() {
        assertEquals("1,000.5", formatNumber("1,000.5"))
        assertEquals("10.75", formatNumber("10.75"))
        assertEquals("0.99", formatNumber("0.99"))
    }

    @Test
    fun `formatNumber should handle empty input`() {
        assertEquals("", formatNumber(""))
    }

    @Test
    fun `formatNumber should return non-numeric strings as-is`() {
        assertEquals("abc", formatNumber("abc"))
        assertEquals("10a", formatNumber("10a"))
        assertEquals("123.45.67", formatNumber("123.45.67"))
    }

    @Test
    fun `formatNumber should handle negative numbers`() {
        assertEquals("-1,000", formatNumber("-1,000"))
        assertEquals("-500.25", formatNumber("-500.25"))
    }

    @Test
    fun `formatNumber should handle very large numbers`() {
        assertEquals("1,000,000", formatNumber("1,000,000"))
        assertEquals("12,345,678.9", formatNumber("12,345,678.9"))
    }
}
