package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun WeatherForecastScreen(navController: NavHostController) {

    val weatherForecast = mutableListOf(
        WeatherData("Mon", "Cloudy", -2.3),
        WeatherData("Tue", "Snowy", -2.0),
        WeatherData("Wed", "Windy", -3.0),
        WeatherData("Thu", "Snowy", -6.2),
        WeatherData("Fri", "Sunny", -12.3),
        WeatherData("Sat", "Cloudy", 2.4),
        WeatherData("Sun", "Rainy", 3.6),
    )
    Column(
        modifier = Modifier
        .fillMaxSize()
)
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Header("Tampere")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(weatherForecast) { WeatherData ->
                WeatherListItem(WeatherData);
            }
        }
    }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = { navController.navigate("currentWeatherScreen") }
            ) {
                Text(stringResource(R.string.current_weather), fontSize = 30.sp)
            }
        }

}
