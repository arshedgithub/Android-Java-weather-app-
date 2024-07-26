package com.example.weatherapp;

public class Weather {

    public int icon;
    public String weather;
    public String desc;
    public String weatherIcon;
    public String temp;
    public String pressure;
    public String humidity;
    public String windSpeed;
    public String weatherDate;

    public Weather(int icon, String weather, String desc, String weatherIcon, String temp, String pressure, String humidity, String windSpeed, String weatherDate) {
        this.icon = icon;
        this.weather = weather;
        this.desc = desc;
        this.weatherIcon = weatherIcon;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherDate = weatherDate;
    }

}
