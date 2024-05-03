package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.classes.WeatherForecastData
import com.example.myapplication.classes.WeatherForecastResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun WeatherForecastScreen(lat: Double, lon: Double) {  //changed from navHostController
    var isLoading by remember { mutableStateOf(true) }
    var fetchError by remember { mutableStateOf<String?>(null) }
    var weatherForecastResponse by remember { mutableStateOf<WeatherForecastResponse?>(null) }

    val isDarkTheme = isSystemInDarkTheme()

    val titleColor = if (isDarkTheme) Color.White else Color.Black



    LaunchedEffect(lat, lon) {
        isLoading = true
        if (lat == 0.0 && lon == 0.0) {
            // Fetch data for Tampere
            try {
                weatherForecastResponse = RetrofitInstance.apiService.fetchWeatherForecastTampere()
                fetchError = null
            } catch (e: Exception) {
                fetchError = e.message
            }
        } else {
            // Fetch data based on latitude and longitude
            try {
                val response = RetrofitInstance.apiService.fetchWeatherForecastByCoordinates(lat, lon)
                weatherForecastResponse = response
                fetchError = null
            } catch (e: Exception) {
                fetchError = e.message
            }
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
                .background(Color(230, 239, 244, 100)),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            //Text(filteredForecastList.toString())
            Text(
                text = stringResource(R.string.forecast),
                modifier = Modifier.padding(top = 16.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
                    .drawBehind {
                        val borderColor = titleColor
                        val strokeWidth = 3.dp.toPx()

                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height), // Start point of the line
                            end = Offset(size.width, size.height), // End point of the line
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Square //  the line cap style if needed
                        )
                    },
                fontSize = 30.sp,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(filteredForecastList) { weatherData ->
                    WeatherForecastListItem(weatherData) }
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



