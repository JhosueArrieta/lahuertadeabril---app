package com.example.lahuertadeabril;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Favourite1Adapter extends RecyclerView.Adapter<Favourite1ViewHolder> {
    private List<Favourite1Item> favourite1Items; // Lista de productos a mostrar en el RecyclerView
    private Activity activity; // Referencia a la actividad que utiliza este adaptador
    private OnItemClickListener favouriteListener;

    // Constructor que recibe la lista de productos y la actividad asociada
    public Favourite1Adapter(List<Favourite1Item> favourite1Items, Activity activity) {
        this.favourite1Items = favourite1Items;
        this.activity = activity;
    }

    // Método llamado cuando se necesita crear un nuevo ViewHolder
    @NonNull
    @Override
    public Favourite1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño de cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite1, parent, false);
        return new Favourite1ViewHolder(view); // Esta línea invoca el constructor del ViewHolder
    }

    // Método llamado cuando se debe asociar un ViewHolder a un elemento específico en la lista
    @Override
    public void onBindViewHolder(@NonNull Favourite1ViewHolder holder, int position) {
        Favourite1Item favourite1Item = favourite1Items.get(position);
        holder.bind(favourite1Item, activity); // Llama al método bind del ViewHolder para mostrar los datos
        final int adapterPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica si hay un Listener y la posición del adaptador es válida
                if (favouriteListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    favouriteListener.onItemClick(adapterPosition); // Llama al método onItemClick del Listener
                }
            }
        });
    }

    // Método que devuelve la cantidad total de elementos en la lista
    @Override
    public int getItemCount() {
        return favourite1Items.size();
    }

    // Interfaz para manejar clics en elementos del RecyclerView
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Método para establecer el Listener para los clics en elementos del RecyclerView
    public void setOnItemClickListener(OnItemClickListener listener) {
        favouriteListener = listener;
    }
}
