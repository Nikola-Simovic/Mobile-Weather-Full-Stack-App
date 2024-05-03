package com.example.myapplication.classes

//The classes here are put in a single file due to the fact that they are all related to a single
//JSON response, just nested, so putting them in a single file was done for the sake of clarity.
data class WeatherForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherForecastData>,  // list of WeatherForecastData objects
    val city: ForecastCity  // city information
)

//  the data class for the list items (weather forecast data)
data class WeatherForecastData(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<ForecastWeather>,
    val clouds: ForecastClouds,
    val wind: ForecastWind,
    val visibility: Int,
    val pop: Double,
    val sys: ForecastSys,
    val dt_txt: String
)

data class ForecastMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
)

data class ForecastWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class ForecastClouds(val all: Int)

data class ForecastWind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class ForecastSys(val pod: String)

data class ForecastCity(
    val id: Int,
    val name: String,
    val coord: ForecastCoord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class ForecastCoord(
    val lat: Double,
    val lon: Double
)
