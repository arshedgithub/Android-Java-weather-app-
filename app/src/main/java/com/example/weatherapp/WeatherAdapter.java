package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WeatherAdapter extends ArrayAdapter {

    private ArrayList<Weather> weatherArrayList;

    public WeatherAdapter(@NonNull Context context, ArrayList<Weather> weatherArrayList) {
        super(context, R.layout.weather_forecast_item, weatherArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather weatherData = (Weather) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_forecast_item, parent, false);
        }

        ImageView weatherIcon = convertView.findViewById(R.id.weather_icon);
        TextView weatherDate = convertView.findViewById(R.id.text_date);
        TextView weatherTemp = convertView.findViewById(R.id.text_temp);

        weatherIcon.setImageResource(weatherData.icon);
        weatherDate.setText(weatherData.weatherDate);
        int temp = (int)(Float.parseFloat(weatherData.temp) - 273);
        weatherTemp.setText(String.valueOf(temp) + '\u2103');

        return convertView;
    }
}
