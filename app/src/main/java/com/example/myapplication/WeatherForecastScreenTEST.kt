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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun WeatherForecastScreenTEST(innerPadding: PaddingValues) {  //changed from navHostController
    var isLoading by remember { mutableStateOf(true) }
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
    } else if (fetchError != null) {
        Text("Error fetching data: $fetchError")
    } else {
        var weatherForecastList = weatherForecastResponse!!.list
        var filteredForecastList = filterWeatherForecastList(weatherForecastList)

        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            //Text(filteredForecastList.toString())

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(filteredForecastList) { weatherData ->
                    WeatherForecastListItem(weatherData)             }
                }


        }

    }

}


fun filterWeatherForecastList(weatherForecastList: List<WeatherForecastData>): List<WeatherForecastData> {
    // the date format to extract the date part from `dt_txt`
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // date format for grouping by the date part
    val dateOnlyFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // grouping the list by date (extracted from `dt_txt`)
    val groupedByDate = weatherForecastList.groupBy {
        // parsing the full date and time from `dt_txt` using `dateFormatter`
        val date: Date? = dateFormatter.parse(it.dt_txt)
        // extracting just the date with `dateOnlyFormatter`
        date?.let { dateOnlyFormatter.format(it) } ?: ""
    }

    // the maximum temperature forecasts
    val maxTemperatureForecasts = mutableListOf<WeatherForecastData>()
    for ((_, group) in groupedByDate) {
        //  the highest `temp_max` temperature in each group
        val maxTempForecast = group.maxByOrNull { it.main.temp_max }
        maxTempForecast?.let {
            // adding the entry with the highest temperature to the result list
            maxTemperatureForecasts.add(it)
        }
    }

    // return the list of maximum temperature forecasts
    return maxTemperatureForecasts
}



