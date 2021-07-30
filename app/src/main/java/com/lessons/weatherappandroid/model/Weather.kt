package com.lessons.nasa.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @field:SerializedName("name") val location: String?,
    val main: Main?, val wind: Wind?, val visibility: String?, val sys: Sys?,
    val timezone: Int
)

data class Main(val temp: String?, val humidity: String?)

data class Wind(val speed: String?)

data class Sys(val sunrise: Long?, val sunset: Long?)

