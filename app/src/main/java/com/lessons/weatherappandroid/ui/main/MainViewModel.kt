package com.lessons.weatherappandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lessons.nasa.model.Weather
import com.lessons.nasa.network.WeatherAPI
import kotlinx.coroutines.launch

sealed class WeatherData {
    data class Success(val serverResponseData: Weather) : WeatherData()
    data class Error(val error: Throwable) : WeatherData()
    data class Loading(val progress: Int?) : WeatherData()
}

class MainViewModel(private val retrofitImpl: WeatherAPI) : ViewModel() {
    val liveDataWeather: MutableLiveData<WeatherData> = MutableLiveData()

    fun getData(zip: String): LiveData<WeatherData> {
        viewModelScope.launch {
            try {
                liveDataWeather.value = WeatherData.Loading(null)
                val resp = retrofitImpl.getWeatherByZip(zip).await()
                liveDataWeather.value = WeatherData.Success(resp)
            } catch (e: Exception) {
                liveDataWeather.value = WeatherData.Error(e)
            }
        }
        return liveDataWeather
    }

}