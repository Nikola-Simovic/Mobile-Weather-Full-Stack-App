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
    var tampere=stringResource(R.string.tampere)

    val weatherForecast = mutableListOf(
        WeatherData(stringResource(R.string.mon), stringResource(R.string.cloudy), -2.3),
        WeatherData(stringResource(R.string.tue), stringResource(R.string.snowy), -2.0),
        WeatherData(stringResource(R.string.wed), stringResource(R.string.windy), -3.0),
        WeatherData(stringResource(R.string.thu), stringResource(R.string.snowy), -6.2),
        WeatherData(stringResource(R.string.fri), stringResource(R.string.sunny), -12.3),
        WeatherData(stringResource(R.string.sat), stringResource(R.string.cloudy), 2.4),
        WeatherData(stringResource(R.string.sun), stringResource(R.string.rainy), 3.6),
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
            Header(stringResource(R.string.tampere))
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
