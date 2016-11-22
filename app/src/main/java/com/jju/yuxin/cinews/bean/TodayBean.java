package com.jju.yuxin.cinews.bean;

/**
 * Created by Administrator on 2016/11/22.
 */

public class TodayBean {
    private String temperature;
    private String weather;
    private String wind;
    private String week;
    private String city;
    private String date_y;

    public TodayBean(String temperature, String weather, String wind, String week, String city, String date_y) {
        this.temperature = temperature;
        this.weather = weather;
        this.wind = wind;
        this.week = week;
        this.city = city;
        this.date_y = date_y;

    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
    }

    @Override
    public String toString() {
        return "TodayBean{" +
                "temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                ", wind='" + wind + '\'' +
                ", week='" + week + '\'' +
                ", city='" + city + '\'' +
                ", date_y='" + date_y + '\'' +
                '}';
    }
}

