package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    HttpURLConnection urlConnection;
    BufferedReader reader;
    String forecastJsonStr;

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
            TextView txtTitle = findViewById(R.id.txtTitle);
            txtTitle.setText(forecastJsonStr);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=london&appid=885b3755570c3648e3bc3453fb4a5f31";
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