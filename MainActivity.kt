package com.example.weatherapplication

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()

    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=52.140469&lon=5.583520&units=metric&appid=a04f27000e3dcb88b712cd5ce3ee5ad7").readText(

                    Charsets.UTF_8

                )

            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)

                println("$jsonObj")
                val current = jsonObj.getJSONObject("current")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)


                val minutely = jsonObj.getJSONArray("minutely").getJSONObject(0)







                val currentdate:Long = jsonObj.getLong("dt")
                println("$currentdate")
                val currentdateText1 = "It is: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(currentdate*1000))


//                val updatedAt:Long = jsonObj.getLong("dt")
//                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = current.getString("temp") + "°C"
                val pressure = current.getString("pressure")
                val humidity = current.getString("humidity")
                val precipitation = minutely.getString("precipitation")


                val weatherDescription = weather.getString("description")


                /* Hier zoeken we de fields op de UI en zetten er de json data in waar het hoort*/

                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.currentdateText1).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(currentdate*1000))
//                findViewById<TextView>(R.id.temp_min).text = tempMin
//                findViewById<TextView>(R.id.temp_max).text = tempMax
//                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
//                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.precipitation).text = precipitation
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                /* als de data in de fields is gezet word de laadbalk ontzichtbaar en kunnen ze het main design zien */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            }
            catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }
}