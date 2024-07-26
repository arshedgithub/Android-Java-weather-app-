package com.example.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    HttpURLConnection urlConnection;
    BufferedReader reader;
    String forecastJsonStr;
    public ArrayList<Weather> weatherArrayList = new ArrayList<>();
    WeatherAdapter weatherAdapter;
    Weather weatherData;
    ActivityMainBinding binding;

    String weather;
    String desc;
    String temp;
    String pressure;
    String humidity;
    String weatherIcon;
    String date;
    String windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FetchData fetchData = new FetchData();
        fetchData.execute();
    }

    public class FetchData extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject forecastResponse = new JSONObject(forecastJsonStr);
                if (Integer.parseInt(forecastResponse.getString("cod")) == 404){
                    Toast.makeText(MainActivity.this, "City Not found", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(forecastResponse.getString("cod")) == 200){
                    JSONArray forecastResponseJSONArray = forecastResponse.getJSONArray("list");
                    for (int i = 0; i < forecastResponseJSONArray.length(); i++) {
                        JSONObject tempObj = forecastResponseJSONArray.getJSONObject(i);

                        weather = tempObj.getJSONArray("weather").getJSONObject(0).getString("main");
                        desc = tempObj.getJSONArray("weather").getJSONObject(0).getString("description");
                        weatherIcon = tempObj.getJSONArray("weather").getJSONObject(0).getString("icon");
                        temp = tempObj.getJSONObject("main").getString("temp");
                        pressure = tempObj.getJSONObject("main").getString("pressure");
                        humidity = tempObj.getJSONObject("main").getString("humidity");
                        windSpeed = tempObj.getJSONObject("wind").getString("speed");
                        date = tempObj.getString("dt_txt");

                        weatherData = new Weather(R.drawable.pic_10d, weather, desc, temp, pressure, humidity, windSpeed, date);
                        weatherArrayList.add(weatherData);
                    }

                    weatherAdapter = new WeatherAdapter(MainActivity.this, weatherArrayList);
                    binding.forecastList.setAdapter(weatherAdapter);
                    binding.forecastList.setClickable(true);

                    binding.forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this, Detailed_weather.class);
                            intent.putExtra("weather", weather);
                            intent.putExtra("desc", desc);
                            intent.putExtra("weatherIcon", weatherIcon);
                            intent.putExtra("temp", temp);
                            intent.putExtra("pressure", pressure);
                            intent.putExtra("humidity", humidity);
                            intent.putExtra("windSpeed", windSpeed);
                            intent.putExtra("date", date);
                            startActivity(intent);
                        }
                    });

                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=london&appid=885b3755570c3648e3bc3453fb4a5f31";
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line1;

                while ((line1 = reader.readLine()) != null) buffer.append(line1 + "\n");
                if (buffer.length() == 0) return null;
                forecastJsonStr = buffer.toString();

                if (inputStream == null) return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

            } catch (IOException e) {
                Log.e("Hi", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null){
                    try {
                        reader.close();
                    } catch (final IOException e){
                        Log.e("Hi", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

}