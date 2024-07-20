package com.example.myweather;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameview, desview;
    Button pastbtn;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameview = itemView.findViewById(R.id.name);
        desview = itemView.findViewById(R.id.description);
//        pastbtn = itemView.findViewById(R.id.pastbtn);
    }
}