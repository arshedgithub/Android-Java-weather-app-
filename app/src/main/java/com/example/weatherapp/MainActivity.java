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

public class MainActivity extends AppCompatActivity {

    HttpURLConnection urlConnection;
    BufferedReader reader;
    String forecastJsonStr;

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
                    JSONObject forecastData = forecastResponse.getJSONArray("list").getJSONObject(0);
                    weatherDescView.setText(forecastData.toString());

                }

//                JSONArray weatherArray = data.getJSONArray("weather");
//                JSONObject weather = weatherArray.getJSONObject(0);
//                JSONObject main = data.getJSONObject("main");

//                String weatherCondition = weather.getString("main");
//                String weatherDesc = weather.getString("description");
//                String weatherIcon = weather.getString("icon");
//                Integer temp = main.getInt("temp");
//                Integer pressure = main.getInt("pressure");
//                Integer humidity = main.getInt("humidity");
//
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