package com.example.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

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

    String[] weather_arr = new String[20];
    String[] weatherDesc_arr = new String[20];
    String[] weatherTemp_arr = new String[20];
    String[] weatherPressure_arr = new String[20];
    String[] weatherHumid_arr = new String[20];
    String[] weatherWind_arr = new String[20];
    String[] weatherIcon_arr = new String[20];
    String[] weatherDate_arr = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FetchData fetchData = new FetchData();
        fetchData.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.info) {
            Intent intent = new Intent(MainActivity.this, Info.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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

                        weather_arr[i] = weather;
                        weatherDesc_arr[i] = desc;
                        weatherTemp_arr[i] = temp;
                        weatherPressure_arr[i] = pressure;
                        weatherHumid_arr[i] = humidity;
                        weatherWind_arr[i] = windSpeed;
                        weatherDate_arr[i] = date;

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
                            intent.putExtra("weather", weather_arr[i]);
                            intent.putExtra("desc", weatherDesc_arr[i]);
                            intent.putExtra("weatherIcon", weatherIcon_arr[i]);
                            intent.putExtra("temp", weatherTemp_arr[i]);
                            intent.putExtra("pressure", weatherPressure_arr[i]);
                            intent.putExtra("humidity", weatherHumid_arr[i]);
                            intent.putExtra("windSpeed", weatherWind_arr[i]);
                            intent.putExtra("date", weatherDate_arr[i]);
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
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=london&cnt=20&appid=885b3755570c3648e3bc3453fb4a5f31";
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