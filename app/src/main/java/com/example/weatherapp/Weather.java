package com.example.weatherapp;

public class Weather {

    public int icon;
    public String weather;
    public String desc;
    public String temp;
    public String pressure;
    public String humidity;
    public String windSpeed;
    public String weatherDate;

    public Weather(int icon, String weather, String desc, String temp, String pressure, String humidity, String windSpeed, String weatherDate) {
        this.icon = icon;
        this.weather = weather;
        this.desc = desc;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherDate = weatherDate;
    }

}
