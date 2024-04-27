package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun CurrentWeatherScreen(navController: NavController,currentPage: Int) { //changed from NavHostController for the button
        val tampere=stringResource(R.string.tampere)
        var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var temperature = -999.99
        var windSpeed = -999.99
        var windDirection = -999
        var weatherDescription="Error"


    LaunchedEffect(Unit){
            weatherResponse = RetrofitInstance.apiService.fetchCurrentWeatherTampere()
            isLoading=false
             }

    if (weatherResponse != null) {
        temperature = weatherResponse!!.main.temp
        windSpeed = weatherResponse!!.wind.speed
        windDirection = weatherResponse!!.wind.deg
        weatherDescription = weatherResponse!!.weather[0].main
    }

        if (isLoading) {
        CircularProgressIndicator()
    }
    else {

        MaterialTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                , horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header(tampere)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$temperature °C",
                        style = TextStyle(
                            color = Color(0xFF115C9E),
                            fontSize = 55.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.sad_document),
                        contentDescription = "Snowy Weather Icon",
                        modifier = Modifier
                            .size(120.dp)
                    )
                }

                Text(
                    text = weatherDescription,
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text("Wind speed: $windSpeed m/s, Direction: $windDirection",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

            /*    CustomButton(
                    text = stringResource(R.string.forecast),
                    onClick = {
                        navController.navigate("weatherForecastScreen")
                    }
                ) */

            }
        }
    }
}