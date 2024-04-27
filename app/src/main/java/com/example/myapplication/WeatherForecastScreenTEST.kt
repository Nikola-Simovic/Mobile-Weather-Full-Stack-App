package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun WeatherForecastScreenTEST(innerPadding: PaddingValues) {  //changed from navHostController
    val tampere=stringResource(R.string.tampere)
    var isLoading by remember { mutableStateOf(true) }
    var weatherResponses by remember { mutableStateOf<List<WeatherResponse>>(emptyList()) }
    var fetchError by remember { mutableStateOf<String?>(null) }
    var weatherForecastResponse by remember { mutableStateOf<WeatherForecastResponse?>(null) }




    val weatherForecast = mutableListOf(
        WeatherData(stringResource(R.string.mon), stringResource(R.string.cloudy), -2.3),
        WeatherData(stringResource(R.string.tue), stringResource(R.string.snowy), -2.0),
        WeatherData(stringResource(R.string.wed), stringResource(R.string.windy), -3.0),
        WeatherData(stringResource(R.string.thu), stringResource(R.string.snowy), -6.2),
        WeatherData(stringResource(R.string.fri), stringResource(R.string.sunny), -12.3),
        WeatherData(stringResource(R.string.sat), stringResource(R.string.cloudy), 2.4),
        WeatherData(stringResource(R.string.sun), stringResource(R.string.rainy), 3.6),
    )


    LaunchedEffect(Unit) {
        try {
            weatherForecastResponse = RetrofitInstance.apiService.fetchWeatherForecastTampere()
        } catch (e: Exception) {
            fetchError = e.message
        }
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    }
    else if (fetchError != null) {
        Text("Error fetching data: $fetchError")
    }
else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(weatherForecast) { weatherData ->
                        WeatherListItem(weatherData)
                    }
                }

            }
        }


}
