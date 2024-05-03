package com.example.myapplication

import com.example.myapplication.classes.WeatherForecastResponse
import com.example.myapplication.classes.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



interface ApiService
{
    @GET("weather?q=Tampere,fi&APPID=57a76f941c3f4809f76030d74cf4b726&units=metric")
    suspend fun fetchCurrentWeatherTampere(): WeatherResponse

    @GET("forecast?q=Tampere,fi&appid=57a76f941c3f4809f76030d74cf4b726&units=metric")
    suspend fun fetchWeatherForecastTampere(): WeatherForecastResponse

    @GET("weather?APPID=57a76f941c3f4809f76030d74cf4b726&units=metric")
    suspend fun fetchCurrentWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherResponse

    @GET("forecast?APPID=57a76f941c3f4809f76030d74cf4b726&units=metric")
    suspend fun fetchWeatherForecastByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherForecastResponse


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
