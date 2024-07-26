package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.databinding.ActivityDetailedWeatherBinding;

public class Detailed_weather extends AppCompatActivity {

    ActivityDetailedWeatherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {
            String date = intent.getStringExtra("date");
            String weather = intent.getStringExtra("weather");
            String desc = intent.getStringExtra("desc");
            String temp = intent.getStringExtra("temp");
            String pressure = intent.getStringExtra("pressure");
            String humidity = intent.getStringExtra("humidity");
            String wind = intent.getStringExtra("windSpeed");

            binding.txtDate.setText(date);
            int tempInC = (int)(Float.parseFloat(temp) - 273);
            binding.txtTemp.setText(String.valueOf(tempInC) + '\u2103');
            binding.txtWeather.setText(weather);
            binding.txtDesc.setText(desc);
            binding.txtPressure.setText(pressure + " Pa");
            binding.txtWind.setText(wind);
            binding.txtHumidity.setText(humidity + "%");

        }
    }
}