package com.example.openweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Weatherdownloader {
    private static final String TAG = "WeatherDownloadRunnable";

    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static Weather weatherObj;




//    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall?lat=41.8675766&lon=-87.616232" +
//            "&appid=6bb64d877702ed121bb0b2a79bbe39d1&units=metric&lang=en&exclude=minutely";

    private static final String iconUrl = "https://openweathermap.org/img/w/";

    //////////////////////////////////////////////////////////////////////////////////
    // Sign up to get your API Key at:  https://home.openweathermap.org/users/sign_up
    private static final String yourAPIKey = "6bfc226f3d6885de5b239a8c33047524";

    //////////////////////////////////////////////////////////////////////////////////

    public static void downloadWeather(MainActivity mainActivityIn,
                                       String city, boolean fahrenheit) {
        String weatherURL = "https://api.openweathermap.org/data/2.5/onecall?";

        mainActivity = mainActivityIn;
        double[] latlong=getLatLon(city);
        if(latlong==null){
            weatherURL+="lat="+String.valueOf(41.8781)+"&lon="+-87.6298+"&appid=6bb64d877702ed121bb0b2a79bbe39d1&units=metric&lang=en&exclude=minutely";
        }
        else {
            weatherURL += "lat=" + String.valueOf(latlong[0])+"&lon=" + String.valueOf(latlong[1])+"&appid=6bb64d877702ed121bb0b2a79bbe39d1&units=metric&lang=en&exclude=minutely";
        }
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        String urlToUse = buildURL.build().toString();
        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString(),mainActivity);

        Response.ErrorListener error =
                error1 -> mainActivity.updateData(null);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(String s,MainActivity mainActivity) {

        try {
            JSONObject jObjMain = new JSONObject(s);
            String city=jObjMain.getString("timezone");

            String icon = jObjMain.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("icon");
            String description = jObjMain.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
            String temp = jObjMain.getJSONObject("current").getString("temp");

            String humidity = jObjMain.getJSONObject("current").getString("humidity");
            String feels_like = jObjMain.getJSONObject("current").getString("feels_like");
            String wind = jObjMain.getJSONObject("current").getString("wind_speed");
            String uv = jObjMain.getJSONObject("current").getString("uvi");
            String visibility= jObjMain.getJSONObject("current").getString("visibility");
            String degree = jObjMain.getJSONObject("current").getString("wind_deg");
            String mor_temp=jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("morn");
            String daytime_temp=jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("day");
            String eve_temp=jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("eve");
            String nig_temp=jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("night");
            String dt = jObjMain.getJSONObject("current").getString("dt");
            String date_offest = jObjMain.getString("timezone_offset");
            String cloud = jObjMain.getJSONObject("current").getString("clouds");

            String min_temp=jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("min");
            String max_temp=jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("max");
            String pop=jObjMain.getJSONArray("daily").getJSONObject(0).getString("pop");
            String gust = "";
//                        if(!jObjMain.getJSONObject("current").getString("wind_gust").equals("null"))
//                        {gust=jObjMain.getJSONObject("current").getString("wind_gust");}
            if(jObjMain.has("wind_gust"))
            {
                gust=jObjMain.getJSONObject("current").getString("wind_gust");
            }
            String rain = "";
            if(jObjMain.has("rain"))
            {
                rain=jObjMain.getJSONObject("current").getString("rain");
            }
            String snow = "";
            if(jObjMain.has("snow"))
            {
                snow=jObjMain.getJSONObject("current").getString("snow");
            }
//            icon = "_"+hourly.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
            int icon2 = mainActivity.getResources().getIdentifier("_"+icon, "drawable",mainActivity.getPackageName());
            weatherObj = new Weather(city,description, temp, humidity, wind, dt,date_offest,feels_like,uv,visibility,mor_temp,daytime_temp,eve_temp,nig_temp,min_temp,max_temp,pop,icon2,degree,"","",gust,cloud,rain,snow);
            weatherObj.temp_desc=s;

        } catch (Exception e) {
            Toast.makeText(mainActivity, "Eoorrrrrr", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private static double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(mainActivity); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            double lat = address.get(0).getLatitude();
            double lon = address.get(0).getLongitude();

            return new double[] {lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }
}
