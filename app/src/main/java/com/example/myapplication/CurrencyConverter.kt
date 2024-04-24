package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier

@Composable
fun CurrencyConverter() {
    var amount by remember { mutableStateOf("") }
    val currencies = listOf("EUR", "USD", "GBP", "SEK")
    val exchangeRates = mapOf("EUR" to 1.0, "USD" to 1.12, "GBP" to 0.90, "SEK" to 9.5)
    var expanded by remember { mutableStateOf(true) }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var convertedAmount by remember { mutableStateOf(0.0) }
    Column(Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Header("EUR to other currency converter")
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = selectedCurrency,
            onValueChange = {},
            label = { Text("Currency") },
            readOnly = true, trailingIcon = {
                Text(text = "▼", Modifier.clickable { expanded = !expanded })
            }
        )
        Box(contentAlignment = Alignment.Center) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        selectedCurrency = currency
                        expanded = false
                    })
            }
        }
    }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            val amountValue=amount.toDoubleOrNull()
            if (amountValue!= null) {
                val rate = exchangeRates[selectedCurrency] ?: 1.0
                convertedAmount = amountValue * rate
            }
            })
        {
            Text ("Convert", fontSize=25.sp)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("Converted amount: ${String.format("%.2f",convertedAmount)}", fontSize=25.sp,)
    }
}


