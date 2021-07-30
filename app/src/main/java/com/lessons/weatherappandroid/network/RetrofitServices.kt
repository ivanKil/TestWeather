package com.lessons.nasa.network


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lessons.weatherappandroid.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class RetrofitServices {
    companion object Factory {
        const val API_KEY: String = BuildConfig.WEATHER_API_KEY
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY_PARAM = "appid";
        private var retrofit: WeatherAPI? = null
        fun create(): WeatherAPI {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createOkHttpClient(ApiInterceptor()))
                    .baseUrl(BASE_URL).build()
                    .create(WeatherAPI::class.java)
            }
            return retrofit!!
        }

        private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            return httpClient.build()
        }

        class ApiInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter(API_KEY_PARAM, API_KEY)
                    .build()
                val newRequest = request.newBuilder().url(url).build()
                return chain.proceed(newRequest)
            }
        }
    }
}