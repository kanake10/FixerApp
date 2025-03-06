package com.example.fixer.ui

import com.example.core.model.CurrencySymbol
import com.example.fixer.ui.components.AmountInputField
import com.example.fixer.ui.components.ConvertButton
import com.example.fixer.ui.components.ConvertedAmountField
import com.example.fixer.ui.components.CurrencyDropdown
import com.example.fixer.ui.components.CurrencyTitle
import com.example.fixer.ui.components.CurrencyTopBar
import com.example.fixer.ui.components.ErrorMessage
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class CurrencyCalculatorUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrencyTopBar_isDisplayed() {
        composeTestRule.setContent { CurrencyTopBar() }

        composeTestRule.onNodeWithContentDescription("Menu").assertExists()
        composeTestRule.onNodeWithText("Sign Up").assertExists()
    }

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

        composeTestRule.onNodeWithText("Converted Amount").assertExists()
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

    @Test
    fun testErrorMessage_isDisplayed() {
        composeTestRule.setContent {
            ErrorMessage(error = "Invalid amount")
        }

        composeTestRule.onNodeWithText("Invalid amount").assertExists()
    }
}
