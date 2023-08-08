package com.aminivan.weatherapp.service

import com.aminivan.weatherapp.response.ResponseFetchWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("forecast")
    fun getForecast(
        @Query("id") cityId: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<ResponseFetchWeather>
}