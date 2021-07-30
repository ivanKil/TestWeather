package com.lessons.weatherappandroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lessons.weatherappandroid.R
import com.lessons.weatherappandroid.snackBarError
import kotlinx.android.synthetic.main.main_fragment.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.liveDataWeather.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData("94043")
        button_search.setOnClickListener { viewModel.getData(zip_code.text.toString()) }
    }

    private fun renderData(data: WeatherData) {
        when (data) {
            is WeatherData.Success -> {
                val serverResponseData = data.serverResponseData
                text_location.text = serverResponseData.location
                text_temperature.text = serverResponseData.main?.temp
                text_wind_speed.text = serverResponseData.wind?.speed
                text_humidity.text = serverResponseData.main?.humidity + "%"
                text_visibilityy.text = serverResponseData.visibility
                text_sunrise.text = if (serverResponseData.sys?.sunrise != null)
                    dateFormat.format(Date(serverResponseData.sys.sunrise * 1000)) else ""
                text_sunset.text = if (serverResponseData.sys?.sunset != null)
                    dateFormat.format(Date(serverResponseData.sys.sunset * 1000))
                else ""
                progress_bar.visibility = View.GONE
            }

            is WeatherData.Loading -> {
                progress_bar.visibility = View.VISIBLE
            }

            is WeatherData.Error -> {
                if (data.error is HttpException) {
                    val er = (data.error as HttpException).response()?.errorBody()?.string()
                    if (er != null)
                        view?.snackBarError(JSONObject(er).getString("message"))
                    else
                        view?.snackBarError(data.error.message ?: "")
                } else
                    view?.snackBarError(data.error.message ?: "")
                progress_bar.visibility = View.GONE
            }
        }
    }
}