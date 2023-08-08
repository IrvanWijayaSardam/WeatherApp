package com.aminivan.weatherapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.aminivan.weatherapp.databinding.ActivityMainBinding
import com.aminivan.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var cityId = ""
    private var units = ""
    private var apikey = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityId = "1642911"
        units = "metric"
        apikey = "1a9c426b7431b7a9d6b5db83f6e1a1d0"

        //ambil data from api
        weatherViewModel.retrieveWeather(cityId, units, apikey)

        //ambil hasil respone
        weatherViewModel.liveDataWeather.observe(this) { weatherResponse ->
            //jika response itdak kosong
            if (weatherResponse != null && weatherResponse.list?.isNotEmpty() == true) {
                val temperatureStringBuilder = StringBuilder()

                val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy")
                val temperatureUnit = "°C"

                val currentDate = Calendar.getInstance()
                var previousDayOfYear = -1

                //loop ke tiap item yang ada di list
                for (item in weatherResponse.list) {
                    val dt = item?.dt?.toLong() ?: continue
                    val temperature = item?.main?.temp
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = dt * 1000
                    //kurangi 1 hari , agar waktu sama dengan dari api
                    calendar.add(Calendar.DAY_OF_YEAR, -1)

                    val currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

                    if (currentDayOfYear != previousDayOfYear && calendar.timeInMillis > currentDate.timeInMillis) {
                        val formattedDate = dateFormat.format(calendar.time)
                        //masukkan ke stringbuilder
                        temperatureStringBuilder.append("$formattedDate : ${String.format("%.2f", temperature)} $temperatureUnit\n")
                        previousDayOfYear = currentDayOfYear
                    }
                }

                //print ke log dan ke layout
                Log.d(TAG, "Weather Forecast :")
                Log.d(TAG, temperatureStringBuilder.toString())
                binding.edtWeather.text = temperatureStringBuilder.toString()
            }
        }
        initListener()
    }

    private fun initListener() {
        //jika diklik maka refresh data baru
        binding.btnRefresh.setOnClickListener {
            weatherViewModel.retrieveWeather(cityId, units, apikey)
        }
    }
}
