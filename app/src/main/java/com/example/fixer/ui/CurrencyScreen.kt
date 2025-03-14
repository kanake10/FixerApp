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

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fixer.ui.components.AmountInputField
import com.example.fixer.ui.components.ConvertButton
import com.example.fixer.ui.components.ConvertedAmountField
import com.example.fixer.ui.components.CurrencySelectionRow
import com.example.fixer.ui.components.CurrencyTitle
import com.example.fixer.ui.components.CurrencyTopBar
import com.example.fixer.ui.components.ErrorMessage
import com.example.fixer.viewmodel.CurrencyViewModel

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var amount by remember { mutableStateOf("0") }
    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("PLN") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { CurrencyTopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CurrencyTitle()
            Spacer(modifier = Modifier.height(60.dp))

            AmountInputField(
                amount = amount,
                fromCurrency,
                onAmountChange = { amount = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ConvertedAmountField(
                convertedAmount = uiState.convertedAmount.toString(),
                toCurrency
            )

            Spacer(modifier = Modifier.height(10.dp))

            uiState.currencySymbols?.let { symbols ->
                CurrencySelectionRow(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    symbols = symbols,
                    onFromCurrencyChange = { fromCurrency = it },
                    onToCurrencyChange = { toCurrency = it }
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            ConvertButton(
                onConvert = {
                    when {
                        amount.isBlank() -> viewModel.setError("Please Enter An Amount")
                        fromCurrency.isBlank() || toCurrency.isBlank() -> {
                            viewModel.setError("Please Select A Currency")
                        }

                        fromCurrency == toCurrency -> {
                            viewModel.setError("Please Select A Different Currency")
                        }

                        else -> {
                            viewModel.setError(null)
                            val amountValue = amount.toDoubleOrNull() ?: 0.0
                            viewModel.convertCurrency(amountValue, fromCurrency, toCurrency)
                        }
                    }
                }
            )

            ErrorMessage(uiState.error)
        }
    }
}
