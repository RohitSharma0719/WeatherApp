package com.example.openweatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String iconUrl = "https://openweathermap.org/img/w/";

    private static final String yourAPIKey = "6bb64d877702ed121bb0b2a79bbe39d1";
    private static final String TAG = "MainActivity";
    private static RequestQueue queue;
    private TextView city_country;
    private SwipeRefreshLayout swiper;
    static boolean fahrenheit = true;
    static boolean swip=false;
    private RecyclerView recyclerView;
    private HorozontalAdapter horozontalAdapter;
    final ArrayList<Weather> weatherHourlyList = new ArrayList<>();
    Weather weather;
    String unit="imperial";
    public static String country="Chicago, Illinois";
   static SharedPreferences sharedPref ;
    private SharedPreferences.Editor editor;
    private TextView city_cou;
    private TextView temp;
    private TextView srise;
    private TextView sset;
    private TextView description;
    private TextView date;
    private TextView morning;
    private TextView city;
    private TextView day;
    private Menu menuItemm;
    private TextView evening;
    private TextView night;
    private TextView wind;
    private TextView rainsnow;
    private TextView eam;
    private TextView fpm;
    private TextView opm;
    private TextView epm;
    private TextView feelslike;
    private TextView uv;
    private TextView visibility;
    private TextView humid;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = findViewById(R.id.temperature);
        srise = findViewById(R.id.sunrise);
        sset = findViewById(R.id.sunset);
        description = findViewById(R.id.condition);
        date = findViewById(R.id.date);
        morning = findViewById(R.id.morning);
        city = findViewById(R.id.city_country);
        evening = findViewById(R.id.evening);
        day = findViewById(R.id.daytime);
        night = findViewById(R.id.night);
        wind = findViewById(R.id.wind);
        rainsnow = findViewById(R.id.rainsnow);
        eam = findViewById(R.id.eightam);
        fpm = findViewById(R.id.fivepm);
        opm = findViewById(R.id.onepm);
        epm = findViewById(R.id.elevenpm);
        feelslike = findViewById(R.id.fLike);
        visibility = findViewById(R.id.visibility);
        humid = findViewById(R.id.humidity);
        image = findViewById(R.id.imageView);
        uv = findViewById(R.id.uv);
        swiper = findViewById(R.id.swiper);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (!hasNetworkConnection()) {
            city.setText("");
            date.setText("No Network Connection");
        } else {

//        downloadWeather(sharedPref.getString("country","Chicago"));
            downloadWeather();
        }
        city_country = findViewById(R.id.city_country);
        recyclerView = findViewById(R.id.horirecycle);

        horozontalAdapter = new HorozontalAdapter(this, weatherHourlyList, fahrenheit);
        recyclerView.setAdapter(horozontalAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horozontalAdapter.notifyDataSetChanged();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!hasNetworkConnection()) {

                    Toast.makeText(MainActivity.this, "No internet connection, try later", Toast.LENGTH_SHORT).show();
                } else {
//                downloadWeather(sharedPref.getString("country","Chicago"));
                    downloadWeather();

                    Toast.makeText(MainActivity.this, "Update", Toast.LENGTH_SHORT).show();
                }
                if(swip==true) {
                swiper.setRefreshing(false);
                }}

        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        menuItemm=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        Log.d(TAG, "handleResult: NULL ActivityResult received");

        if(menuItem.getItemId()==R.id.location)
        {
            if(!hasNetworkConnection()) {
                Toast.makeText(MainActivity.this, "No Internet! Can't reach Location now, try later", Toast.LENGTH_SHORT).show();
            }
            else
            {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String s= et.getText().toString();
                    String bool=getLatLon(et.getText().toString());
                    if(bool=="True")
                    {

                        downloadWeather();}}
                    }
            );

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setTitle("Enter a Location:");
            builder.setMessage("For US locations, enter as 'City' or 'City, State'\n\nFor International locations, enter as 'City, Country'");

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }}
        else if(menuItem.getItemId()==R.id.details)
        {
            if(!hasNetworkConnection()) {
                Toast.makeText(MainActivity.this, "No Internet! try later", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, WeatherDetails.class);
                intent.putExtra("s", weather.temp_desc);
                startActivity(intent);
            }
        }
        else if(menuItem.getItemId()==R.id.fehranhite)
        {
            if(!hasNetworkConnection()) {
                Toast.makeText(MainActivity.this, "No Internet! try later", Toast.LENGTH_SHORT).show();
            }
            else {
            editor = sharedPref.edit();
            fahrenheit=!fahrenheit;
            if(!fahrenheit)
            {
                fahrenheit = false;
                editor.putBoolean("FAHRENHEIT", false);
            }
            else
            {
                fahrenheit = true;
                editor.putBoolean("FAHRENHEIT", true);
            }
            editor.apply();
//            data(sharedPref.getString("country","Chicago"));
//            downloadWeather(sharedPref.getString("country","Chicago"));
            downloadWeather();
            horozontalAdapter.notifyDataSetChanged();
            Toast.makeText(this, "unit changed", Toast.LENGTH_SHORT).show();
        }}
        else
        {
            Log.d(TAG, "onOptionsItemSelected: ");
            return true;
        }
        return false;
    }
    public void doNewTemp(View v) {
        doDownload();
    }

    private void doDownload() {
        String cityName = city_country.getText().toString().trim().replaceAll(", ", ",");

        Weatherdownloader.downloadWeather(this, cityName, fahrenheit);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(Weather weather) {
        if (weather == null) {
            Toast.makeText(this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
            return;
        }
//        double degree=Double.parseDouble(weather.getDegree());
        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(Long.parseLong(weather.getDate()) + Long.parseLong(weather.getOffset()), 0, ZoneOffset.UTC);
        LocalDateTime ldt1 =
                LocalDateTime.ofEpochSecond(Long.parseLong(weather.getSunrise()) + Long.parseLong(weather.getOffset()), 0, ZoneOffset.UTC);
        LocalDateTime ldt2 =
                LocalDateTime.ofEpochSecond(Long.parseLong(weather.getSunset()) + Long.parseLong(weather.getOffset()), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        String formattedTimeString1 = ldt1.format(dtf1);
        String formattedTimeString2 = ldt2.format(dtf2);
        String degree=getDirection(Double.parseDouble(weather.getDegree()));
        temp.setText(String.format("%.0f°" + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getTemp())));
        srise.setText("Sunrise: "+formattedTimeString1);
        sset.setText("Sunset: "+formattedTimeString2);
        int temp_cloud=Integer.parseInt(weather.getCloud());
        if(temp_cloud==0)
        {
            description.setText(String.format("%s", weather.getDescription()));
        }
        else {
            description.setText(String.format("%s", weather.getDescription()) + "(" + temp_cloud + "% Clouds)");
        }

        date.setText(formattedTimeString);

//        city.setText(sharedPref.getString("country","Chicago"));
        city.setText(sharedPref.getString("country",country)+"   ");
        morning.setText(String.format("%.0f°" + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getMor())));

        day.setText(String.format("%.0f°" + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getDay())));

        evening.setText(String.format("%.0f°" + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getEve())));

        night.setText(String.format("%.0f°" + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getNight())));

        if(weather.getGust().equals("null"))
        {
            wind.setText(String.format("Winds: "+degree+" at %.0f " + (fahrenheit ? "mph" : "mps"), Double.parseDouble(weather.getWind())));
        }
        else
        {
            wind.setText(String.format("Winds: "+degree+" at %.0f " + (fahrenheit ? "mph" : "mps"), Double.parseDouble(weather.getWind()))+", " +Double.parseDouble(weather.getGust())+ (fahrenheit ? " mph" : " mps")+" gusts");
        }
        if(!weather.getRain().equals("null"))
        {
            rainsnow.setText("Rain last 1 hr: "+weather.getRain()+"mm");
        }
        else if(!weather.getSnow().equals("null"))
        {
            rainsnow.setText("Snow last 1 hr: "+weather.getSnow()+"mm");
        }
        else {
            rainsnow.setText("");
        }

        epm.setText("11pm");
        eam.setText("8am");
        fpm.setText("5pm");
        opm.setText("1pm");


//        wind.setText("Winds: "+degree+" at "+weather.getWind()+" mph");

        uv.setText("UV Index: "+weather.getUV());
        feelslike.setText(String.format("Feels Like %.0f° " + (fahrenheit ? "F" : "C"), Double.parseDouble(weather.getFeels())));

        if(fahrenheit)
        {
            double visi=Double.parseDouble(weather.getVisibility());
            visi=visi/1609;
            visibility.setText("Visibility: "+new DecimalFormat("##.##").format(visi)+" mi");
        }
        else
        {
            double visi=Double.parseDouble(weather.getVisibility());
            visi=visi/1000;
            visibility.setText("Visibility: "+new DecimalFormat("##.##").format(visi)+" km");
        }
//        visibility.setText("Visibility: "+weather.getVisibility());

        humid.setText(String.format(Locale.getDefault(), "Humidity: %.0f%%", Double.parseDouble(weather.getHumidity())));

        int iconResId = this.getResources().getIdentifier("_"+weather.getIcon(),
                "drawable", this.getPackageName());

        image.setImageResource(weather.getIcon());
        if(fahrenheit) {
            menuItemm.getItem(0).setIcon(R.drawable.units_f);
        }
        else {
            menuItemm.getItem(0).setIcon(R.drawable.units_c);
        }
//        image.setImageBitmap(weather.geBitmap());
    }

    private String getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address = geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                Toast.makeText(this, "Unable to fine location, try again later", Toast.LENGTH_SHORT).show();
                return null;
            }
            float lat = (float) address.get(0).getLatitude();
            float lon = (float) address.get(0).getLongitude();
            List<Address> addresses;
           addresses = geocoder.getFromLocationName(lat+","+lon, 10);
           country=userProvidedLocation;
           if(addresses.size()!=0)
           {
               if(addresses.get(0).getCountryCode().equals("US"))
               {
                   country=(addresses.get(0).getLocality() +", "+addresses.get(0).getAdminArea());
               }
               else {
                   country = (addresses.get(0).getLocality() == null ? (addresses.get(0).getAdminArea() != null ? addresses.get(0).getAdminArea() + ", " : "") : addresses.get(0).getLocality() + ", ") + addresses.get(0).getCountryName();
               }}
            editor = sharedPref.edit();
            editor.putString("latitude", String.valueOf(lat));
            editor.putString("longitude", String.valueOf(lon));
            editor.putString("country",country);
            editor.apply();
            return "True";
        } catch (IOException e) {
            return null;
        }

    }
    public void downloadWeather() {
        String weatherURL = "https://api.openweathermap.org/data/2.5/onecall?";
        queue = Volley.newRequestQueue(this);
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendQueryParameter("lat",sharedPref.getString("latitude","41.8781"));
        buildURL.appendQueryParameter("lon", sharedPref.getString("longitude","-87.6298"));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        buildURL.appendQueryParameter("units", (fahrenheit ? "imperial" : "metric"));
        buildURL.appendQueryParameter("lang", "en");
        buildURL.appendQueryParameter("exclude","minutely");
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> {
                    try {
                        JSONObject jObjMain = new JSONObject(response.toString());
                        String City = jObjMain.getString("timezone");
                        String sunrise = jObjMain.getJSONObject("current").getString("sunrise");
                        String sunset = jObjMain.getJSONObject("current").getString("sunset");
                        String icon = jObjMain.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("icon");
                        String description = jObjMain.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
                        String temp = jObjMain.getJSONObject("current").getString("temp");
                        String humidity = jObjMain.getJSONObject("current").getString("humidity");
                        String feels_like = jObjMain.getJSONObject("current").getString("feels_like");
                        String wind = jObjMain.getJSONObject("current").getString("wind_speed");
                        String gust = "null";
//                        if(!jObjMain.getJSONObject("current").getString("wind_gust").equals("null"))
//                        {gust=jObjMain.getJSONObject("current").getString("wind_gust");}
                        if (jObjMain.getJSONObject("current").has("wind_gust")) {
                            gust = jObjMain.getJSONObject("current").getString("wind_gust");
                        }
                        String cloud = jObjMain.getJSONObject("current").getString("clouds");
                        String rain = "null";
                        if (jObjMain.getJSONObject("current").has("rain")) {
                            rain = jObjMain.getJSONObject("current").getJSONObject("rain").getString("1h");
                        }
                        String snow = "null";
                        if (jObjMain.getJSONObject("current").has("snow")) {
                            snow = jObjMain.getJSONObject("current").getJSONObject("snow").getString("1h");
                        }
                        String degree = jObjMain.getJSONObject("current").getString("wind_deg");
                        String uv = jObjMain.getJSONObject("current").getString("uvi");
                        String visibility = jObjMain.getJSONObject("current").getString("visibility");
                        String mor_temp = jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("morn");
                        String daytime_temp = jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("day");
                        String eve_temp = jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("eve");
                        String nig_temp = jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("night");
                        String dt = jObjMain.getJSONObject("current").getString("dt");
                        String date_offest = jObjMain.getString("timezone_offset");
                        String min_temp = jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("min");
                        String max_temp = jObjMain.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getString("max");
                        String pop = jObjMain.getJSONArray("daily").getJSONObject(0).getString("pop");
//                        int popp=Integer.parseInt(pop);
//                        popp=popp*100;
                        int icon2 = this.getResources().getIdentifier("_" + icon, "drawable", this.getPackageName());
                        Weather weather = new Weather(City, description, temp, humidity, wind, dt, date_offest, feels_like, uv, visibility, mor_temp, daytime_temp, eve_temp, nig_temp, min_temp, max_temp, pop, icon2, degree, sunrise, sunset, gust, cloud, rain, snow);
                        Weather.temp_desc = response.toString();
                        JSONArray jsonArray=jObjMain.getJSONArray("hourly");
                        weatherHourlyList.clear();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            int abc=this.getResources().getIdentifier("_"+jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"),
                                    "drawable", this.getPackageName());
                            weatherHourlyList.add(
                                    new Weather(sharedPref.getString("country","Chicago, Illinois"),jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"),
                                            jsonArray.getJSONObject(i).getString("temp"),
                                            "e",
                                            "e",
                                            jsonArray.getJSONObject(i).getString("dt"),jObjMain.getString("timezone_offset"),
                                            jObjMain.getJSONObject("current").getString("dt"),
                                            jsonArray.getJSONObject(i).getString("uvi"),
                                            "df", "", "", "", "", "", "",
                                            jsonArray.getJSONObject(i).getString("pop"),
                                            abc,"","","","","","",""
                                    ));
                        }
                        horozontalAdapter.notifyDataSetChanged();
                        updateData(weather);
                    } catch (Exception e) {
                        Toast.makeText(this, "Eoorrrrrr", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                };

        Response.ErrorListener error =
                error1 -> this.updateData(null);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        swip=true;
    }


private String getDirection(double degrees) {
    if (degrees >= 337.5 || degrees < 22.5)
        return "N";
    if (degrees >= 22.5 && degrees < 67.5)
        return "NE";
    if (degrees >= 67.5 && degrees < 112.5)
        return "E";
    if (degrees >= 112.5 && degrees < 157.5)
        return "SE";
    if (degrees >= 157.5 && degrees < 202.5)
        return "S";
    if (degrees >= 202.5 && degrees < 247.5)
        return "SW";
    if (degrees >= 247.5 && degrees < 292.5)
        return "W";
    if (degrees >= 292.5 && degrees < 337.5)
        return "NW";
    return "X"; // We'll use 'X' as the default if we get a bad value
}

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        Long calender=(Long.parseLong(weatherHourlyList.get(position).getDate())*1000L)+Long.parseLong(weatherHourlyList.get(position).getOffset());
        ContentUris.appendId(builder,calender);

        startActivity(new Intent(Intent.ACTION_VIEW).setData(builder.build()));
    }
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());

    }

}
