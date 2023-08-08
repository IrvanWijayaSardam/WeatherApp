package com.aminivan.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.aminivan.weatherapp.databinding.ActivityMainBinding
import com.aminivan.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.ViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cityId = "1642911"
        val units = "metric"
        val apiKey = "1a9c426b7431b7a9d6b5db83f6e1a1d0"

        weatherViewModel.retrieveWeather(cityId,units, apiKey)

        weatherViewModel.liveDataWeather.observe(this, { weatherResponse ->
            // Handle the weather data update here
            if (weatherResponse != null && weatherResponse.list?.isNotEmpty() == true) {
                val temperature = weatherResponse.list[0]?.main?.temp
                binding.edtWeather.text = temperature.toString()
            }
        })

    }
}
