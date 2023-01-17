package com.prannat.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //kotlin syntax for getting temp text by id
        val tempTextView = findViewById<TextView>(R.id.tempTextView)
        //java syntax: TempView tempTextView = (TextView) findViewById(R.id.tempTextView)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)
        val weatherTextView = findViewById<TextView>(R.id.weatherDescTextView)
        val cityTextView = findViewById<TextView>(R.id.cityTextView)
        val weatherImageView = findViewById<ImageView>(R.id.imageView2)
        weatherImageView.setImageResource(R.drawable.icon_sunny)

        dateTextView.text = getCurrentDate()



        //json request using volley for kotlin

        val url = "https://api.openweathermap.org/data/2.5/weather?q=Halifax&appid=d7a0a1ec5e18dc4f6a5023699ede1757&units=metric"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                //setting the converted text from json via volley to app text field

                val mainJSONObject: JSONObject = response.getJSONObject("main")
                val temp: String = mainJSONObject.getDouble("temp").roundToInt().toString()
                tempTextView.text = temp

                val weatherArray: JSONArray = response.getJSONArray("weather")
                val firstWeatherObject: JSONObject = weatherArray.getJSONObject(0)
                val weatherDescription: String = firstWeatherObject.getString("description")
                weatherTextView.text = weatherDescription

                val city: String = response.getString("name")
                cityTextView.text = city

                val timeZone: Int = response.getInt("timezone")
                val timeZoneInMinutes = timeZone/60
                val timeString: String = timeZoneInMinutes.toString()
                timeTextView.text = timeString


                //image set as per weather
                val iconResourceId = resources.getIdentifier("icon_" + weatherDescription.replace(" ",""),"drawable",packageName)
                weatherImageView.setImageResource(iconResourceId)
            },
            { error ->
                // TODO: Handle error
            }
        )

// Access the RequestQueue through your singleton class.
        var queue: RequestQueue? = null
        queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)

    }
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat: SimpleDateFormat = SimpleDateFormat("EEEE, MMM dd")

        return dateFormat.format(calendar.time)

    }
}