package com.example.openweatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalHolder extends RecyclerView.ViewHolder {
    TextView day;
    TextView time,temp,desc;
    ImageView image;
    public HorizontalHolder(@NonNull View itemView) {
        super(itemView);
        day=itemView.findViewById(R.id.whatday);
        time=itemView.findViewById(R.id.whattime);
        temp=itemView.findViewById(R.id.whattemp);
        desc=itemView.findViewById(R.id.whatdesc);
        image=itemView.findViewById(R.id.pictural);
    }
}
