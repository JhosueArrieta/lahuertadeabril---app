package com.example.lahuertadeabril;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Product1Adapter extends RecyclerView.Adapter<Product1ViewHolder> {
    private List<Product1Item> product1Items; // Lista de productos a mostrar en el RecyclerView
    private Activity activity; // Referencia a la actividad que utiliza este adaptador
    private OnItemClickListener productListener;

    // Constructor que recibe la lista de productos y la actividad asociada
    public Product1Adapter(List<Product1Item> product1Items, Activity activity) {
        this.product1Items = product1Items;
        this.activity = activity;
    }

    // Método llamado cuando se necesita crear un nuevo ViewHolder
    @NonNull
    @Override
    public Product1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product1, parent, false);
        return new Product1ViewHolder(view); // Esta línea invoca el constructor del ViewHolder
    }

    // Método llamado cuando se debe asociar un ViewHolder a un elemento específico en la lista
    @Override
    public void onBindViewHolder(@NonNull Product1ViewHolder holder, int position) {
        Product1Item product1Item = product1Items.get(position);
        holder.bind(product1Item, activity); // Llama al método bind del ViewHolder para mostrar los datos
        final int adapterPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica si hay un Listener y la posición del adaptador es válida
                if (productListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    productListener.onItemClick(adapterPosition); // Llama al método onItemClick del Listener
                }
            }
        });
    }

    // Método que devuelve la cantidad total de elementos en la lista
    @Override
    public int getItemCount() {
        return product1Items.size();
    }

    // Interfaz para manejar clics en elementos del RecyclerView
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Método para establecer el Listener para los clics en elementos del RecyclerView
    public void setOnItemClickListener(OnItemClickListener listener) {
        productListener = listener;
    }
}
