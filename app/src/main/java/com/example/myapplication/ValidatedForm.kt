package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedForm() {
    var name by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(true) }
    var emailError by remember { mutableStateOf(true) }
    var nameError by remember { mutableStateOf(true) }
    val buttonEnabled = !nameError && !emailError && !passwordError

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally)
    {
        Header("Registration form")
        Spacer(modifier = Modifier.height(35.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {name = it
                            nameError=name.length<1},
            label = { Text("First and last name") }
        )
        if(nameError) {
            Text("Name must not be empty")
        }
        OutlinedTextField(
            value = emailAddress,
            onValueChange = {emailAddress = it
                            emailError=!emailAddress.contains("@")},
            label = { Text("Your email address") },
        )
        if(emailError) {
            Text("Email address must contain @")
        }
        OutlinedTextField(
            value = password,
            onValueChange = {password = it
                            passwordError=password.length<8},
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
            )
        if(passwordError) {
            Text("Password must be at least 8 characters")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick= {

        },enabled = buttonEnabled){
            Text("Validate", fontSize=30.sp)
        }
    }
}