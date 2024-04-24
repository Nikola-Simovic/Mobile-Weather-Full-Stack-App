package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiCalculator() {
    var heightText by remember {mutableStateOf("")}
    var weightText by remember {mutableStateOf("")}
    var bmi by remember {mutableDoubleStateOf(0.0)}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally)
    {
        Header("BMI Calculator")
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = heightText,
            onValueChange = {heightText = it},
            label = {Text("Your height (cm)")},
            keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(40.dp),
            value = weightText,
            onValueChange = {weightText = it},
            label = {Text("Your weight (kg)")},
            keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(text="Your BMI: ${String.format("%.1f",bmi)}", fontSize=40.sp)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick= {
            val height = heightText.toDoubleOrNull()
            val weight = weightText.toDoubleOrNull()
            if (weight!= null && height!=null)
            {
                bmi=weight/(height/100*height/100)
            }
        }){
            Text("Calculate BMI", fontSize=40.sp)
        }
    }
}