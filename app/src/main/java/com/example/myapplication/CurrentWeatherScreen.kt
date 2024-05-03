package com.example.myapplication

import android.content.Context
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
import kotlin.math.roundToInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.classes.WeatherForecastResponse
import com.example.myapplication.classes.WeatherResponse
import java.util.Locale


@Composable
fun CurrentWeatherScreen(lat: Double, lon: Double) {
    var city=stringResource(R.string.tampere)
    var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }         //simple remembers to update the app
    var weatherForecastResponse by remember { mutableStateOf<WeatherForecastResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var temperature = -999.99   //default values, made clearly wrong, so that the app will stay stable and the error will be clear
    var windSpeed = -999.98
    var windDirection = -997
    var humidity = -996
    var feelsLike = -999.95
    var weatherDescription="Error"

    var fetchError by remember { mutableStateOf<String?>(null) }

    val isDarkTheme = isSystemInDarkTheme()

    val titleColor = if (isDarkTheme) Color.White else Color.Black
    val context= LocalContext.current




    LaunchedEffect(lat, lon) {   //starts off with data for tampere by default
        isLoading = true
        if (lat == 0.0 && lon == 0.0) {
            // Fetch data for Tampere
            try {
                weatherResponse = RetrofitInstance.apiService.fetchCurrentWeatherTampere()
                weatherForecastResponse = RetrofitInstance.apiService.fetchWeatherForecastTampere()
                fetchError = null
            } catch (e: Exception) {
                fetchError = e.message
            }
        } else {
            // fetches data based on latitude and longitude if the user allows for the location to be tracked
            try {
                val response = RetrofitInstance.apiService.fetchCurrentWeatherByCoordinates(lat, lon)
                weatherResponse = response
                val forecastResponse = RetrofitInstance.apiService.fetchWeatherForecastByCoordinates(lat, lon)
                weatherForecastResponse=forecastResponse
                fetchError = null
            } catch (e: Exception) {   //error checks for safety
                fetchError = e.message
            }
        }
        isLoading = false
    }


    if (weatherResponse != null) {     //error checks insuring that we get a weather response
        temperature = weatherResponse!!.main.temp
        windSpeed = weatherResponse!!.wind.speed
        windDirection = weatherResponse!!.wind.deg
        weatherDescription = weatherResponse!!.weather[0].main
        humidity = weatherResponse!!.main.humidity
        feelsLike=weatherResponse!!.main.feels_like
        city=weatherResponse!!.name

    }


    if (isLoading) {    //more safety checks along with a circular indicator to show loading
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
                        painter = painterResource(getBackgroundImageResource(weatherDescription)), //images based on the weather
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(350.dp, 250.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .border(
                                width = 10.dp,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(30.dp)
                            )
                    )

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
                    text = weatherDescriptionTextFormatter(weatherDescription,city,context), //due to finnish having a different word order this was required for localization
                    color = titleColor,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(   //a lazy column in order to load the data and allow scrolling in the app
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    item {
                        ScrollableRow(weatherForecastResponse)  //a scrollable row that holds the daily weather and remembers how far we scrolled
                        //with a clear display and images/times/temperature

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(      //a row with two boxes combo that holds the data of the feels-like and humidity values and descriptions
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
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
                                        text = stringResource(R.string.feels_like),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
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
                                        text = stringResource(R.string.humidity),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))  //a bit of space between the scrollable rows

                        Row(   //the second row-boxes combo that holds the wind speed and direction data and descriptions
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
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
                                        text = stringResource(R.string.wind_speed),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp)
                                    .height(90.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = degreesToCompass(context,windDirection),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.direction),
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
fun getBackgroundImageResource(description:String): Int  //in order to get the proper background image, a value is returned based on the description
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
fun degreesToCompass(context: Context, degrees: Int): String { //mapping all of the compass values for english and finnish for localization
    val directions = arrayOf(context.getString(R.string.northShortcut),
        context.getString(R.string.northEastShortcut),
        context.getString(R.string.eastShortcut),
        context.getString(R.string.southEastShortcut),
        context.getString(R.string.southShortcut),
        context.getString(R.string.southWestShortcut),
        context.getString(R.string.westShortcut),
        context.getString(R.string.northWestShortcut))
    val index = ((degrees / 45.0) + 0.5).toInt() % 8   //the direction received is in degrees which needed to be transformed into a direction
    return directions[index]
}


fun weatherDescriptionTextFormatter(description: String, city: String, context: Context) : String{  //a description formatter, due to the style choices made, the app
    val locale: Locale = context.resources.configuration.locales.get(0)  //needed to have a different syntax in Finnish, so a formatter was created in order to address that
    val descriptionToResourceId = when (description) {
        "Clear" -> R.string.clear
        "Clouds" -> R.string.cloudy
        "Rain" -> R.string.rainy
        "Snow" -> R.string.snowy
        else -> R.string.error
    }

    val localizedDescription = context.getString(descriptionToResourceId)

    return if (locale.language == "en") {
        "$description in $city"
    } else {
        "$localizedDescription ${city}ssa"
    }
}
