package com.example.myapplication

import androidx.compose.runtime.Composable

@Composable
fun getImageResource(description:String): Int  //this code chooses the icons for the app displays based on the description
{
    val clouds = "Clouds"
    val snow = "Snow"
    val clear = "Clear"
    val rain = "Rain"
    val wind = "Wind"

    return when (description) {
        snow -> R.drawable.snowy_image
        clouds -> R.drawable.cloudy_image
        clear -> R.drawable.sunny_image
        rain -> R.drawable.rainy_image
        wind -> R.drawable.windy_image
        else -> R.drawable.clicked_refresh_icon
    }
}