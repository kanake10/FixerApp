package com.example.fixer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.core.model.CurrencySymbol
import com.example.fixer.R
import com.example.fixer.viewmodel.CurrencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(viewModel: CurrencyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var amount by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Currency Calculator", color = Color.Blue, fontSize = 20.sp) },
                navigationIcon = {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Green)
                },
                actions = {
                    Text("Sign Up", color = Color.Green, fontSize = 16.sp)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Amount Input Field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount in EUR") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Text("EUR", color = Color.Gray) }
            )

            // Currency Dropdown
            uiState.currencySymbols?.let { symbols ->
                CurrencyDropdown(
                    selectedCurrency = selectedCurrency,
                    symbols = symbols,
                    onCurrencySelected = { selectedCurrency = it }
                )
            }

            // Convert Button
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    viewModel.convertCurrency(amountValue, "EUR", selectedCurrency)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Convert", color = Color.White)
            }

            // Converted Amount Display
            OutlinedTextField(
                value = uiState.convertedAmount.toString(),
                onValueChange = {},
                label = { Text("Converted Amount") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Text(selectedCurrency, color = Color.Gray) }
            )

            // Error Message
            uiState.error?.let {
                Text(it, color = Color.Red)
            }
        }
    }
}

/** Dropdown for selecting a currency */
@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    symbols: List<CurrencySymbol>,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Currency") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            symbols.forEach { symbol ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CurrencyFlag(symbol.code) // Load currency flag dynamically
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${symbol.code}")
                        }
                    },
                    onClick = {
                        onCurrencySelected(symbol.code)
                        expanded = false // Close dropdown
                    }
                )
            }
        }
    }
}

/** Loads and displays the currency flag */
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