package com.aminivan.weatherapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aminivan.weatherapp.response.ResponseFetchWeather
import com.aminivan.weatherapp.service.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(var api: ApiService) : ViewModel() {
    var liveDataWeather = MutableLiveData<ResponseFetchWeather?>()


    fun retrieveWeather(id: String,units: String, appid: String) {
        val client = api.getForecast(id,units,appid)
        client.enqueue(object : Callback<ResponseFetchWeather> {
            override fun onResponse(
                call: Call<ResponseFetchWeather>,
                response: Response<ResponseFetchWeather>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!.list
                    if (responseBody != null) {
                        Log.d(ContentValues.TAG, "onResponse: ${responseBody}")
                        liveDataWeather.postValue(response.body())
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ResponseFetchWeather>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

}