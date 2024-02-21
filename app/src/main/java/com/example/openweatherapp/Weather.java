package com.example.openweatherapp;

import android.graphics.Bitmap;

public class Weather {
//    private final String city;
//    private final String country;
//    private final String conditions;
public static String temp_desc;
    private final String description;
    private final String temp;
    private final String humidity;
    private final String wind;
    private final String date,offset;
    private final String feelslike;
    private final String uv;
    private final String visibility;
    private final String mor,day,eve,night,minTemp,maxTemp,sunrise,sunset;
    private Bitmap bitmap;
    private final String city,gust,cloud;
            String pop,rain,snow;
    int icon;
    String degree;

    Weather(String city,String description, String temp, String humidity, String wind, String date,String offest,String feelslike, String uv, String visibility,
            String mor_temp,String daytime_temp,String eve_temp,String nig_temp,String min_temp,String max_temp, String pop, int icon,String degree
            ,String sunrise, String sunset,String gust,String cloud,String rain,String snow) {
//        this.city = city;
//        this.country = country;
//        this.conditions = conditions;
        this.description = description;
        this.temp = temp;
        this.humidity = humidity;
        this.wind = wind;
        this.date = date;
        this.feelslike=feelslike;
        this.uv=uv;
        this.visibility=visibility;
        this.mor=mor_temp;this.day=daytime_temp;this.eve=eve_temp;this.night=nig_temp;
        this.minTemp=min_temp;this.maxTemp=max_temp;
        this.pop=pop;
        this.icon=icon;
        this.city=city;
        this.degree=degree;
        this.offset=offest;this.sunrise=sunrise;this.sunset=sunset;
        this.gust=gust;
        this.cloud=cloud;
        this.rain=rain;
        this.snow=snow;
    }
    String getFeels(){return feelslike;}

    String getDescription() {
        String words[]=description.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    String getTemp() {
        return temp;
    }

    String getHumidity() {
        return humidity;
    }

    String getWind() {
        return wind;
    }

    String getDate() {
        return date;
    }
    String getUV(){return uv;}
    String getVisibility(){return visibility;}
    String getMor(){return mor;}
    String getDay(){
        return day;}
    String getEve(){return eve;}
    String getNight(){return night;}
    String getMinTemp(){return minTemp;}
    String getMaxTemp(){return maxTemp;}
    int getIcon()
    {
        return icon;
    }
    String getPop()
    {
        return pop;
    }
    Bitmap getBitmap() {
        return bitmap;
    }
    String getCity(){
        return city;}
    String getDegree()
    {
        return degree;
    }
String getOffset(){
        return offset;
    }
    String getSunrise()
    {
        return sunrise;
    }
    String getSunset()
    {
        return sunset;
    }
    String getGust()
    {
        return gust;
    }
    String getCloud()
    {
        return cloud;
    }
    String getRain()
    {
        return rain;
    }
    String getSnow()
    {
        return snow;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
