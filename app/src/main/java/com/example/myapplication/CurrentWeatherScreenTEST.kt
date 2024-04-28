package com.example.myapplication

import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherScreenTEST(innerPadding: PaddingValues) {
    val tampere=stringResource(R.string.tampere)
    var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var temperature = -999.99
    var windSpeed = -999.99
    var windDirection = -999
    var humidity = -999
    var feelsLike = -999.99
    var weatherDescription="Error"

    var fetchError by remember { mutableStateOf<String?>(null) }



    LaunchedEffect(Unit) {
        try {
            weatherResponse = RetrofitInstance.apiService.fetchCurrentWeatherTampere()
        } catch (e: Exception) {
            fetchError = e.message
        }
        isLoading = false
    }



    if (weatherResponse != null) {
        temperature = weatherResponse!!.main.temp
        windSpeed = weatherResponse!!.wind.speed
        windDirection = weatherResponse!!.wind.deg
        weatherDescription = weatherResponse!!.weather[0].main
        humidity = weatherResponse!!.main.humidity
        feelsLike=weatherResponse!!.main.feels_like

    }


    if (isLoading) {
        CircularProgressIndicator()
    }
    else if (fetchError != null) {
        Text("Error fetching data: $fetchError")
    }
    else {

        MaterialTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(230, 239, 244, 100))
                , horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Image
                    Image(
                        painter = painterResource(getBackgroundImageResource(weatherDescription)),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(350.dp,250.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .border(
                                width = 10.dp,  // Width of the border
                                color = MaterialTheme.colorScheme.secondaryContainer,  // Color of the border (white)
                                shape = RoundedCornerShape(30.dp)  // Shape of the border with rounded corners
                            )
                    )

                    // Text centered at the bottom of the image
                    Text(
                        text = "${temperature.roundToInt()} °C",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 55.sp,
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }


                Text(
                    text = weatherDescription,
                    color = Color.Black,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                                    .height(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${feelsLike.roundToInt()} °C",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Feels like",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                                    .height(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$humidity%",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Humidity",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                                    .height(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${windSpeed.roundToInt()} m/s",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Wind Speed",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                                    .height(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$windDirection°",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Direction",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))


            }
        }
    }
}
@Composable
fun getBackgroundImageResource(description:String): Int
{
    val clouds = "Clouds"
    val snow = "Snow"
    val clear = "Clear"
    val rain = "Rain"
    val wind = "Wind"

    return when (description) {
        snow -> R.drawable.snow_background
        clouds -> R.drawable.clouds_background
        clear -> R.drawable.sun_background
        rain -> R.drawable.rain_background
        wind -> R.drawable.windy_image
        else -> R.drawable.clicked_refresh_icon
    }
}
