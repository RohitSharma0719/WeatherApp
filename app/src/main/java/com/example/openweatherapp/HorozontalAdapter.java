package com.example.openweatherapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class HorozontalAdapter extends RecyclerView.Adapter<HorizontalHolder>{
    private final  ArrayList<Weather> weatherList;
    MainActivity mainActivity;
    boolean feh;
    public HorozontalAdapter(MainActivity mainActivity, ArrayList<Weather> weatherList,boolean fehranhite) {
        this.mainActivity=mainActivity;
        this.weatherList=weatherList;
        this.feh=fehranhite;
    }

    @NonNull
    @Override
    public HorizontalHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontaldata, parent, false);

        itemView.setOnClickListener((View.OnClickListener) mainActivity);
//        itemView.setOnLongClickListener((View.OnLongClickListener) mainAct);

        return new HorizontalHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalHolder holder, int position) {
//        holder.day.setText(weatherList.get(position).get);
        LocalDateTime ldt =
                LocalDateTime.ofEpochSecond(Long.parseLong(weatherList.get(position).getDate()) + Long.parseLong(weatherList.get(position).getOffset()), 0, ZoneOffset.UTC);
        LocalDateTime ldt1 =
                LocalDateTime.ofEpochSecond(Long.parseLong(weatherList.get(position).getFeels()) + Long.parseLong(weatherList.get(position).getOffset()), 0, ZoneOffset.UTC);

        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());

        String formattedTimeString1 = ldt.format(dtf1);
        String format=ldt1.format(dtf1);
        String formattedTimeString2 = ldt.format(dtf2);

        holder.image.setImageResource(weatherList.get(position).getIcon());
        holder.temp.setText(String.format("%.0f° " + (MainActivity.fahrenheit ? "F" : "C"),
                Double.parseDouble(weatherList.get(position).getTemp())));
        holder.desc.setText(weatherList.get(position).getDescription());
        holder.time.setText(formattedTimeString2);
        if(format.equals(formattedTimeString1))
        {
            holder.day.setText("Today");
        }
        else
        {
            holder.day.setText(formattedTimeString1);
        }
//        holder.day.setText(formattedTimeString1);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }



}
