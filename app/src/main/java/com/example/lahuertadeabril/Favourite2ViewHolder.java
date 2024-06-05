package com.example.lahuertadeabril;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class Favourite2ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView nombreTextView;

    public Favourite2ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.productImage);
        nombreTextView = itemView.findViewById(R.id.productName);
    }

    public void bind(Favourite2Item product2Item, Activity activity) {
        nombreTextView.setText(product2Item.getNombre());
        Glide.with(activity)
                .load(product2Item.getImagenUrl())
                .into(imageView);
    }
}
