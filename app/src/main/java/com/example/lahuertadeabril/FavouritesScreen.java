package com.example.lahuertadeabril;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouritesScreen extends AppCompatActivity {

    private Context context = this;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private Activity activity = this;
    private TextView gadisTitle, mercadonaTitle;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_screen);

        // Inicializar vistas
        gadisTitle = findViewById(R.id.gadisTitle);
        recyclerView1 = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        mercadonaTitle = findViewById(R.id.mercadonaTitle);
        queue = Volley.newRequestQueue(this);

        // Configurar la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.greenwater)); // Reemplaza your_custom_color con el color que desees
            actionBar.setTitle("La Huerta de Abril");
        }

        // Configurar la navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_favourites) {
                return true;
            }
            if (item.getItemId() == R.id.navigation_search) {
                startActivity(new Intent(FavouritesScreen.this, SearchScreen.class));
                return true;
            }
            if (item.getItemId() == R.id.navigation_options) {
                startActivity(new Intent(FavouritesScreen.this, AccountScreen.class));
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_favourites);
        // Iniciamos petición
        favouritesGadisProducts();
        favouritesMercadonaProducts();

    }

    private void favouritesGadisProducts() {

        // Obtener el token de sesión de SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString("VALID_TOKEN", "");

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                "http://10.0.2.2:8000/v1/favourites_1/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Poner títulos a los recyclerViews
                            gadisTitle.setText("GADIS");

                            // Parsear los productos del JSON
                            List<Favourite1Item> favourite1Items = parseProductos(response.getJSONArray("favourites"));

                            // Configurar RecyclerView
                            Favourite1Adapter adapter1 = new Favourite1Adapter(favourite1Items, activity);
                            recyclerView1.setAdapter(adapter1);
                            recyclerView1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

                            adapter1.setOnItemClickListener(new Favourite1Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    // Obtener el objeto SearchAlbumItem correspondiente a la posición del clic
                                    Favourite1Item clickedItem = favourite1Items.get(position);
                                    // Obtener el ID del álbum seleccionado
                                    String favouriteId = clickedItem.getId();
                                    // Iniciar la actividad AlbumInfoScreen y pasar el ID como extra
                                    Intent intent = new Intent(FavouritesScreen.this, ProductDetail1.class);
                                    intent.putExtra("PRODUCT1_ID", favouriteId);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("sessionToken", sessionToken);
                return headers;
            }
        };

        // Añadir la solicitud a la cola
        queue.add(request1);
    }
    // Mercadona
    private void favouritesMercadonaProducts() {

        // Obtener el token de sesión de SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString("VALID_TOKEN", "");

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET,
                "http://10.0.2.2:8000/v1/favourites_2/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Poner títulos a los recyclerViews
                            mercadonaTitle.setText("MERCADONA");

                            // Parsear los productos del JSON
                            List<Favourite2Item> favourite2Items = parse2Productos(response.getJSONArray("favourites"));

                            // Configurar RecyclerView
                            Favourite2Adapter adapter2 = new Favourite2Adapter(favourite2Items, activity);
                            recyclerView2.setAdapter(adapter2);
                            recyclerView2.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

                            adapter2.setOnItemClickListener(new Favourite2Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    // Obtener el objeto SearchAlbumItem correspondiente a la posición del clic
                                    Favourite2Item clickedItem = favourite2Items.get(position);
                                    // Obtener el ID del álbum seleccionado
                                    String favouriteId = clickedItem.getId();
                                    // Iniciar la actividad AlbumInfoScreen y pasar el ID como extra
                                    Intent intent = new Intent(FavouritesScreen.this, ProductDetail2.class);
                                    intent.putExtra("PRODUCT2_ID", favouriteId);
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("sessionToken", sessionToken);
                return headers;
            }
        };

        // Añadir la solicitud a la cola
        queue.add(request2);
    }



    // Método para parsear los productos del JSONArray
    private List<Favourite1Item> parseProductos(JSONArray jsonArray) throws JSONException {
        List<Favourite1Item> favourite1Items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String nombre = jsonObject.getString("nombre");
            String imageUrl = jsonObject.getString("imagen_url");
            Favourite1Item favourite1Item = new Favourite1Item(id, nombre, imageUrl);
            favourite1Items.add(favourite1Item);
        }
        return favourite1Items;
    }

    // Método para parsear los productos del JSONArray
    private List<Favourite2Item> parse2Productos(JSONArray jsonArray) throws JSONException {
        List<Favourite2Item> favourite2Items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String nombre = jsonObject.getString("nombre");
            String imageUrl = jsonObject.getString("imagen_url");
            Favourite2Item favourite2Item = new Favourite2Item(id, nombre, imageUrl);
            favourite2Items.add(favourite2Item);
        }
        return favourite2Items;
    }
}