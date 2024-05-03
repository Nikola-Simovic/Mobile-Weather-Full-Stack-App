package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.classes.WeatherForecastResponse

//A scrollable row for the daily temperatures, so the user can see up to 24h in advance
//with timestamps, temperatures and the weather itself displayed on the currentWeather screen
@Composable
fun ScrollableRow(weatherForecastResponse: WeatherForecastResponse?) {
    var extractedText = "N/A"  //default values
    var extractedTemp = -99.99
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .horizontalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(8) { index ->
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (weatherForecastResponse != null) {     //making sure that the response is valid
                        val text = weatherForecastResponse.list[index].dt_txt
                        extractedTemp = weatherForecastResponse.list[index].main.temp
                        if (index == 0) {   //for the first one, "now" or a localized equivalent is used while the others are timestamped
                            extractedText = stringResource(R.string.now)
                        } else {
                            extractedText = text.substring(11..12)
                        }

                        // the column arranges the time,icon and temperature to be displayed.
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = extractedText,
                                fontSize = 14.sp,
                            )

                            Image(
                                painter = painterResource(id = getImageResource(weatherForecastResponse.list[index].weather[0].main)),
                                contentDescription = "Weather Icon",
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "${extractedTemp.toInt()}°",
                                fontSize = 14.sp,
                            )




                        }
                    } else {
                        Text("Error Fetching Data")  //in case of an error, an error message is displayed
                    }
                }
            }
        }
    }
}