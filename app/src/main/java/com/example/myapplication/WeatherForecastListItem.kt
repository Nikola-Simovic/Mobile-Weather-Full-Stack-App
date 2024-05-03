package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.classes.WeatherForecastData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeatherForecastListItem(weatherForecastData: WeatherForecastData) {
    val primaryContainerColor = MaterialTheme.colorScheme.secondaryContainer

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(primaryContainerColor, RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(text = translateDateToDay(weatherForecastData.dt_txt), fontSize = 25.sp, modifier = Modifier.weight(2f))
            Spacer(modifier = Modifier.weight(3f))

            Column(modifier = Modifier.weight(3f))
            {

                Text(text=weatherForecastItemDescriptionFormatter(weatherForecastData.weather[0].main), fontSize=15.sp)

                Text(text = "${weatherForecastData.main.temp_max.roundToInt()} °C", fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = getImageResource(weatherForecastData.weather[0].main)),
                contentDescription = weatherForecastData.weather[0].main,
                modifier = Modifier
                    .size(40.dp)
            )


        }
    }
}
@Composable
fun weatherForecastItemDescriptionFormatter(description: String): String {
    return when (description) {
        "Clear" -> stringResource(id = R.string.clear)
        "Rain" -> stringResource(id = R.string.rainy)
        "Snow" -> stringResource(id = R.string.snowy)
        "Clouds" -> stringResource(id = R.string.cloudy)
        else -> stringResource(id = R.string.error)
    }
}

fun translateDateToDay(dtTxt: String): String {
    //  the date format for parsing the input date string
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    //  the date format for translating the date to the day of the week
    val outputDateFormat = SimpleDateFormat("EEE", Locale.getDefault())

    return try {
        // parsing the input date string to a Date object
        val date: Date =
            inputDateFormat.parse(dtTxt) ?: throw IllegalArgumentException("Invalid date format")

        // formatting the Date object as the day of the week
        val dayOfWeek = outputDateFormat.format(date)

        // Capitalize the first letter of the day of the week
        dayOfWeek.replaceFirstChar { it.uppercase() }

    } catch (e: Exception) {
        // handling any parsing or formatting exceptions
        println("Error translating date to day: ${e.localizedMessage}")
        // returning a default value in case of an error
        "Invalid date"

    }
}

