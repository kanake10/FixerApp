package com.example.fixer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var fromCurrency by remember { mutableStateOf("EUR") }
    var toCurrency by remember { mutableStateOf("PLN") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Spacer(modifier = Modifier.width(0.dp)) },
                navigationIcon = {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Green)
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
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Currency",
                    color = Color.Blue,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Calculator",
                    color = Color.Blue,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Enter Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = uiState.convertedAmount.toString(),
                onValueChange = {},
                label = { Text("Converted Amount") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Text(toCurrency, color = Color.Gray) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                uiState.currencySymbols?.let { symbols ->
                    CurrencyDropdown(
                        selectedCurrency = fromCurrency,
                        symbols = symbols,
                        onCurrencySelected = { fromCurrency = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.convert),
                    contentDescription = "Swap Currencies",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(32.dp)
                )

                uiState.currencySymbols?.let { symbols ->
                    CurrencyDropdown(
                        selectedCurrency = toCurrency,
                        symbols = symbols,
                        onCurrencySelected = { toCurrency = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    when {
                        amount.isBlank() -> {
                            viewModel.setError("Please Enter An Amount")
                            return@Button
                        }
                        fromCurrency.isBlank() || toCurrency.isBlank() -> {
                            viewModel.setError("Please Select A Currency")
                            return@Button
                        }
                        fromCurrency == toCurrency -> {
                            viewModel.setError("Please Select A Currency")
                            return@Button
                        }
                        else -> {
                            viewModel.setError(null)
                            val amountValue = amount.toDoubleOrNull() ?: 0.0
                            viewModel.convertCurrency(amountValue, fromCurrency, toCurrency)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                shape = RectangleShape
            ) {
                Text(
                    text = "Convert",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            uiState.error?.let { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
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
                    Text(selectedCurrency, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
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
                label = { Text("Search currency") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            val filteredSymbols = symbols.filter {
                it.code.contains(searchQuery, ignoreCase = true) ||
                        it.name.contains(searchQuery, ignoreCase = true)
            }

            filteredSymbols.forEach { symbol ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CurrencyFlag(symbol.code)
                            Spacer(modifier = Modifier.width(8.dp))
                            Row {
                                Text(
                                    symbol.code,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    symbol.name,
                                    fontSize = 12.sp
                                )
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
                    text = "No results found",
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
