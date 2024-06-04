package com.example.lahuertadeabril;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class Product1ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView nombreTextView;

    public Product1ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.productImage);
        nombreTextView = itemView.findViewById(R.id.productName);
    }

    public void bind(Product1Item product1Item, Activity activity) {
        nombreTextView.setText(product1Item.getNombre());
        Glide.with(activity)
                .load(product1Item.getImagenUrl())
                .into(imageView);
    }
}
