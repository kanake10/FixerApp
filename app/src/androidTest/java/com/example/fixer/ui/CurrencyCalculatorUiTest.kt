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
package com.example.fixer.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.kanake.core.model.CurrencySymbol
import com.kanake.ratex.ui.components.AmountInputField
import com.kanake.ratex.ui.components.ConvertButton
import com.kanake.ratex.ui.components.ConvertedAmountField
import com.kanake.ratex.ui.components.CurrencyDropdown
import com.kanake.ratex.ui.components.CurrencyTitle
import org.junit.Rule
import org.junit.Test

class CurrencyCalculatorUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrencyTitle_isDisplayed() {
        composeTestRule.setContent { CurrencyTitle() }

        composeTestRule.onNodeWithText("Currency").assertExists()
        composeTestRule.onNodeWithText("Calculator").assertExists()
    }

    @Test
    fun testAmountInputField_updatesText() {
        var inputText = ""
        composeTestRule.setContent {
            AmountInputField(amount = inputText, fromCurrency = "EUR") { inputText = it }
        }

        composeTestRule.onNodeWithText("Enter Amount").assertExists()
        composeTestRule.onNodeWithText("EUR").assertExists()

        composeTestRule.onNodeWithText("Enter Amount").performTextInput("100")
        assert(inputText == "100")
    }

    @Test
    fun testConvertedAmountField_isDisplayedAndDisabled() {
        composeTestRule.setContent {
            ConvertedAmountField(convertedAmount = "450", toCurrency = "PLN")
        }

        composeTestRule.onNodeWithTag("convertedAmountField").assertExists()
        composeTestRule.onNodeWithText("450").assertExists()
        composeTestRule.onNodeWithText("PLN").assertExists()
        composeTestRule.onNodeWithText("450").assertIsNotEnabled()
    }

    @Test
    fun testConvertButton_isClickable() {
        var clicked = false
        composeTestRule.setContent {
            ConvertButton { clicked = true }
        }

        composeTestRule.onNodeWithText("Convert").assertExists()
        composeTestRule.onNodeWithText("Convert").performClick()

        assert(clicked)
    }

    @Test
    fun testCurrencyDropdown_isDisplayedAndSelectable() {
        val symbols = listOf(
            CurrencySymbol("USD", "United States Dollar"),
            CurrencySymbol("KES", "Kenyan Shilling")
        )
        var selectedCurrency = "USD"

        composeTestRule.setContent {
            CurrencyDropdown(
                selectedCurrency = selectedCurrency,
                symbols = symbols,
                onCurrencySelected = { selectedCurrency = it }
            )
        }

        composeTestRule.onNodeWithText("USD").assertExists()
        composeTestRule.onNodeWithText("USD").performClick()
        composeTestRule.onNodeWithText("Search currency").assertExists()
        composeTestRule.onNodeWithText("United States Dollar").assertExists()
        composeTestRule.onNodeWithText("Kenyan Shilling").performClick()

        assert(selectedCurrency == "KES")
    }
}
