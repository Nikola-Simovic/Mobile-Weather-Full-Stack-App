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
fun WeatherForecastScreen(lat: Double, lon: Double) {  //can receive latitude and longitude for precise location display
    var isLoading by remember { mutableStateOf(true) }
    var fetchError by remember { mutableStateOf<String?>(null) }
    var weatherForecastResponse by remember { mutableStateOf<WeatherForecastResponse?>(null) }

    val isDarkTheme = isSystemInDarkTheme()

    val titleColor = if (isDarkTheme) Color.White else Color.Black



    LaunchedEffect(lat, lon) { //on-launch and on lat/lon changes checks and management as well as error handling
        isLoading = true
        if (lat == 0.0 && lon == 0.0) { //these are the default values so this will always display tampere on startup
            // fetches data for Tampere
            try {
                weatherForecastResponse = RetrofitInstance.apiService.fetchWeatherForecastTampere()
                fetchError = null
            } catch (e: Exception) {
                fetchError = e.message
            }
        } else {
            // fetch data based on latitude and longitude if it changes
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
        CircularProgressIndicator()  //an indicator at the start to show it loading
    } else if (fetchError != null) {  //error check
        Text("Error fetching data: $fetchError")
    } else { //in case everything runs smoothly, a list of forecast list items is displayed with the following code
        var weatherForecastList = weatherForecastResponse!!.list
        var filteredForecastList = filterWeatherForecastList(weatherForecastList)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(230, 239, 244, 100)),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = stringResource(R.string.forecast),
                modifier = Modifier.padding(top = 16.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
                    .drawBehind {
                        val borderColor = titleColor
                        val strokeWidth = 3.dp.toPx()

                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height), //start point of the line
                            end = Offset(size.width, size.height), //end point of the line
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Square
                        )
                    },
                fontSize = 30.sp,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            LazyColumn(  //a lazy column of forecasts so that they can be scrolled if needed
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(filteredForecastList) { weatherData -> //uses a filter to display the list
                    WeatherForecastListItem(weatherData) }
                }


        }

    }

}


//Due to the nature of the API, that is the daily values being locked behind a paywall, the API used sends JSON
//data for the next ~4 days in 3h intervals, so in order to make the forecast page work, they needed to be filtered
//by day, after which I decided to take the one where the highest temperature occurs which is usually mid-day
//and use that as the daily weather icon, since I think it's quite representable for the days weather
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

    // returns the list of maximum temperature forecasts
    return maxTemperatureForecasts
}



