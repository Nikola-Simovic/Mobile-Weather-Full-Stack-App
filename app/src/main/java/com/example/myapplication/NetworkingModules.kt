package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Rain(
    val `1h`: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class WeatherForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherResponse>  // List of weather data
)



interface ApiService
{
    @GET("todos/1")
    suspend fun fetchTodo1(): TodoItem

    @GET("todos")
    suspend fun fetchTodos(): List<TodoItem>

    @GET("weather?q=Tampere,fi&APPID=57a76f941c3f4809f76030d74cf4b726&units=metric")
    suspend fun fetchCurrentWeatherTampere(): WeatherResponse

    @GET("forecast?q=Tampere,fi&appid=57a76f941c3f4809f76030d74cf4b726&units=metric")
    suspend fun fetchWeatherForecastTampere(): WeatherForecastResponse
}

object RetrofitInstance{
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit by lazy{
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
