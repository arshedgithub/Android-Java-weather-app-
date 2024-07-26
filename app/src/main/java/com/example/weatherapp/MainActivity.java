package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    String weather;
    String desc;
    String temp;
    String pressure;
    String humidity;
    String weatherIcon;
    String date;
    String windSpeed;

    int[] forecastIcons = new int[20];
    String[] forecastTime = new String[20];
    String[] forecastTemp = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
            TextView weatherTitleView = findViewById(R.id.weatherCondition);
            TextView weatherDescView = findViewById(R.id.weatherDesc);

            try {
                JSONObject forecastResponse = new JSONObject(forecastJsonStr);
                if (Integer.parseInt(forecastResponse.getString("cod")) == 404){
                    Toast.makeText(MainActivity.this, "City Not found", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(forecastResponse.getString("cod")) == 200){
                    JSONArray forecastResponseJSONArray = forecastResponse.getJSONArray("list");
                    for (int i = 0; i < forecastResponseJSONArray.length(); i++) {
                        JSONObject tempObj = forecastResponseJSONArray.getJSONObject(i);
                        weather = tempObj.getJSONObject("weather").getString("main");
                        desc = tempObj.getJSONObject("weather").getString("description");
                        weatherIcon = tempObj.getJSONObject("weather").getString("icon");
                        temp = tempObj.getJSONObject("main").getString("temp");
                        pressure = tempObj.getJSONObject("main").getString("pressure");
                        humidity = tempObj.getJSONObject("main").getString("humidity");
                        windSpeed = tempObj.getJSONObject("wind").getString("speed");
                        date = tempObj.getString("dt_txt");
                    }

                }

//                weatherTitleView.setText(weatherCondition);
//                weatherDescView.setText(weatherDesc);

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

    public void showWeatherList(JSONObject data){

    }
}