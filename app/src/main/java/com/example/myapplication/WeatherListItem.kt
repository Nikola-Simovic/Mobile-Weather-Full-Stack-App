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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherListItem(weatherData: WeatherData){
    val cloudy = stringResource(R.string.cloudy)
    val snowy = stringResource(R.string.snowy)
    val sunny = stringResource(R.string.sunny)
    val rainy = stringResource(R.string.rainy)
    val windy = stringResource(R.string.windy)

    fun getImageResource(description:String): Int
    {
        return when (description) {
            snowy -> R.drawable.snowy_image
            cloudy -> R.drawable.cloudy_image_2
            sunny -> R.drawable.sunny_image
            rainy -> R.drawable.rainy_image
            windy -> R.drawable.windy_image
            else -> R.drawable.clicked_refresh_icon
        }
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.LightGray, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(text = weatherData.day, fontSize = 30.sp, modifier = Modifier.weight(2f))
        Spacer(modifier = Modifier.weight(3f))

        Column(modifier = Modifier.weight(3f))
        {

            Text(text=weatherData.description, fontSize=15.sp)

            Text(text = "${weatherData.temperature} °C", fontSize = 15.sp)
        }
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = getImageResource(weatherData.description)),
            contentDescription = weatherData.description,
            modifier = Modifier
                .size(40.dp)
        )
    }

}

@Preview
@Composable
fun WeatherListItemPreview()
{
    WeatherListItem(WeatherData("Mon","Sunny",2.5))

}