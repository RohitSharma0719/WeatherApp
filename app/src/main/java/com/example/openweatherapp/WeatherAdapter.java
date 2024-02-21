package com.example.openweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {
    private final  ArrayList<Weather> newNoteList;
    MainActivity mainActivity;
    WeatherDetails weatherDetails;
    Weather weather;
    private boolean fahrenheit = true;
    public WeatherAdapter(WeatherDetails weatherDetails, ArrayList<Weather> weatherList) {
        this.weatherDetails=weatherDetails;
        this.newNoteList=weatherList;
    }
    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.week_details, parent, false);
        return new WeatherHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
//        LocalDateTime ldt = LocalDateTime.ofEpochSecond(Long.parseLong(newNoteList.get(position).getDate()) + Long.parseLong(newNoteList.get(position).getOffset()), 0, ZoneOffset.UTC);
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE MM/dd", Locale.getDefault());
//        String formattedTimeString = ldt.format(dtf);
//        weatherDetails.setTitle(newNoteList.get(position).getCity());
//        weatherDetails.setTitle("OpenWeather Application");

            holder.uv.setText("UV Index: "+newNoteList.get(position).getUV());
            holder.minmax.setText(String.format("%.0f°" + (MainActivity.fahrenheit? "F" : "C"), Double.parseDouble(newNoteList.get(position).getMaxTemp()))+"/"+String.format("%.0f°" + (MainActivity.fahrenheit? "F" : "C"), Double.parseDouble(newNoteList.get(position).getMinTemp())));
            holder.description.setText(newNoteList.get(position).getDescription());
//            holder.daily.setText(formattedTimeString);
        holder.daily.setText(date(Long.parseLong(newNoteList.get(position).getDate()) + Long.parseLong(newNoteList.get(position).getOffset())));
            holder.day.setText(String.format("%.0f°" + (MainActivity.fahrenheit? "F" : "C"), Double.parseDouble(newNoteList.get(position).getDay())));
            holder.morning.setText(String.format("%.0f°" + (MainActivity.fahrenheit? "F" : "C"), Double.parseDouble(newNoteList.get(position).getMor())));
            holder.evening.setText(String.format("%.0f°" + (MainActivity.fahrenheit? "F" : "C"), Double.parseDouble(newNoteList.get(position).getEve())));
            holder.night.setText(String.format("%.0f°" + (MainActivity.fahrenheit ? "F" : "C"), Double.parseDouble(newNoteList.get(position).getNight())));
            holder.imageView.setImageResource(newNoteList.get(position).getIcon());
            holder.percip.setText("("+new DecimalFormat("##.##").format(Double.parseDouble(newNoteList.get(position).getPop())*100)+"% precip.)");

    }

    @Override
    public int getItemCount() {
        return newNoteList.size();
    }
    private String date(long l)
    {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(l, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE MM/dd", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }
}
