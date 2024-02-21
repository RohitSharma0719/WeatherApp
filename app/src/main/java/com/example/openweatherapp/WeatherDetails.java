package com.example.openweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherDetails extends AppCompatActivity {
    MainActivity mainActivity;
    private static final String TAG = "WeatherDetails";
    private RecyclerView recyclerView;
    WeatherAdapter weatherAdapter;
    private final ArrayList<Weather> weatherList = new ArrayList<>();
    JSONObject jObjMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle);

        Intent intent = getIntent();
        if (intent.hasExtra("s")) {
            String temp_weather = intent.getStringExtra("s");
            try {

                jObjMain = new JSONObject(temp_weather);
                JSONArray jObjMain2 = jObjMain.getJSONArray("daily");

                for (int i = 0; i < jObjMain2.length(); i++) {
                    int abc=this.getResources().getIdentifier("_"+jObjMain2.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"),
                            "drawable", this.getPackageName());
                    weatherList.add(new Weather(MainActivity.sharedPref.getString("country",MainActivity.country),jObjMain2.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"),
                            "e", "e", "e", jObjMain2.getJSONObject(i).getString("dt"),jObjMain.getString("timezone_offset") ,"gydsa", jObjMain2.getJSONObject(i).getString("uvi"), "df",
                            jObjMain2.getJSONObject(i).getJSONObject("temp").getString("morn")
                            , jObjMain2.getJSONObject(i).getJSONObject("temp").getString("day"), jObjMain2.getJSONObject(i).getJSONObject("temp").getString("eve"),
                            jObjMain2.getJSONObject(i).getJSONObject("temp").getString("night"), jObjMain2.getJSONObject(i).getJSONObject("temp").getString("min"),
                                    jObjMain2.getJSONObject(i).getJSONObject("temp").getString("max"),jObjMain2.getJSONObject(i).getString("pop"),
                                    abc,"","","","","","","")
                            );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setTitle(weatherList.get(0).getCity());
            recyclerView = findViewById(R.id.reycleview);
            weatherAdapter = new WeatherAdapter(this, weatherList);
            recyclerView.setAdapter(weatherAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            weatherAdapter.notifyDataSetChanged();
        }

    }
}
