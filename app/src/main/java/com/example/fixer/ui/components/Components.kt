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
package com.example.fixer.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.CurrencySymbol
import com.example.fixer.R
import com.example.fixer.util.formatNumber

@Composable
fun CurrencyTitle() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.currency),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.calculator),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun AmountInputField(
    amount: String,
    fromCurrency: String,
    onAmountChange: (String) -> Unit
) {
    OutlinedTextField(
        value = formatNumber(amount),
        onValueChange = { input ->
            val rawInput = input.replace(Regex("[^\\d.]"), "")
            onAmountChange(rawInput)
        },
        label = {
            Text(
                text = stringResource(id = R.string.enter_amount),
                fontWeight = FontWeight.Bold)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Text(
                fromCurrency,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
fun ConvertedAmountField(
    convertedAmount: String,
    toCurrency: String
) {
    val formattedAmount = formatNumber(convertedAmount)

    OutlinedTextField(
        value = formattedAmount,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(
                text = stringResource(id = R.string.converted_amount),
                fontWeight = FontWeight.Bold
            )
        },
        enabled = true,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("convertedAmountField"),
        trailingIcon = {
            Text(
                text = toCurrency,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
fun CurrencySelectionRow(
    fromCurrency: String,
    toCurrency: String,
    symbols: List<CurrencySymbol>,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyDropdown(
            selectedCurrency = fromCurrency,
            symbols = symbols,
            onCurrencySelected = onFromCurrencyChange,
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = painterResource(id = R.drawable.convert),
            contentDescription = stringResource(id = R.string.swap_currencies),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(32.dp),
            colorFilter = ColorFilter.tint(Color.Green)
        )

        CurrencyDropdown(
            selectedCurrency = toCurrency,
            symbols = symbols,
            onCurrencySelected = onToCurrencyChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ConvertButton(onConvert: () -> Unit) {
    Button(
        onClick = onConvert,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
        shape = RectangleShape
    ) {
        Text(
            text = stringResource(id = R.string.convert),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    symbols: List<CurrencySymbol>,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    //  Debugging log
    LaunchedEffect(symbols) {
        println("Currency symbols: $symbols")
        println("Symbols size: ${symbols.size}")
    }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RectangleShape,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CurrencyFlag(selectedCurrency)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        selectedCurrency,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(stringResource(id = R.string.search_currency)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )


            val filteredSymbols = if (searchQuery.isBlank()) {
                symbols
            } else {
                symbols.filter {
                    it.code.contains(searchQuery, ignoreCase = true) ||
                            it.name.contains(searchQuery, ignoreCase = true)
                }
            }


            filteredSymbols.forEach { symbol ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CurrencyFlag(symbol.code)
                            Spacer(modifier = Modifier.width(8.dp))
                            Row {
                                Text(symbol.code, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(symbol.name, fontSize = 12.sp)
                            }
                        }
                    },
                    onClick = {
                        onCurrencySelected(symbol.code)
                        expanded = false
                        searchQuery = ""
                    }
                )
            }

            if (filteredSymbols.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_results_found),
                    modifier = Modifier.padding(8.dp),
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CurrencyFlag(currencyCode: String) {
    val flagUrl = "https://flagcdn.com/w40/${currencyCode.take(2).lowercase()}.png"

    AsyncImage(
        model = flagUrl,
        contentDescription = "$currencyCode Flag",
        modifier = Modifier.size(24.dp),
        placeholder = painterResource(id = R.drawable.ic_launcher_background),
        error = painterResource(id = R.drawable.ic_launcher_background)
    )
}
