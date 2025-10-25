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
package com.kanake.ratex.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kanake.ratex.ui.components.AmountInputField
import com.kanake.ratex.ui.components.ConvertButton
import com.kanake.ratex.ui.components.ConvertedAmountField
import com.kanake.ratex.ui.components.CurrencySelectionRow
import com.kanake.ratex.ui.components.CurrencyTitle
import com.kanake.ratex.viewmodel.CurrencyViewModel

@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var amount by rememberSaveable { mutableStateOf("100") }
    var fromCurrency by rememberSaveable { mutableStateOf("EUR") }
    var toCurrency by rememberSaveable { mutableStateOf("USD") }
    val focusManager = LocalFocusManager.current

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(14.dp)
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

            AnimatedVisibility(visible = amount.isNotBlank()) {
                ConvertButton(
                    onConvert = {
                        val amountValue = amount.toDoubleOrNull() ?: 0.0
                        viewModel.convertCurrency(amountValue, fromCurrency, toCurrency)
                    }
                )
            }
        }
    }
}
