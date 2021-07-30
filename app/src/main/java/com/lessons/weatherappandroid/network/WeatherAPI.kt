package com.lessons.nasa.network

import com.lessons.nasa.model.Weather
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    fun getWeatherByZip(@Query("zip") date: String): Deferred<Weather>
}