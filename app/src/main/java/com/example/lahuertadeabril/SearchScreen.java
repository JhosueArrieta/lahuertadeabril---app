package com.example.lahuertadeabril;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class SearchScreen extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private EditText searchView;
    private Activity activity = this;
    private TextView gadisTitle;
    private TextView mercadonaTitle;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        // Inicializar vistas
        searchView = findViewById(R.id.searchView);
        gadisTitle = findViewById(R.id.gadisTitle);
        recyclerView1 = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        mercadonaTitle = findViewById(R.id.mercadonaTitle);
        queue = Volley.newRequestQueue(this);

        // Configurar la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.greenwater));
            actionBar.setTitle("La Huerta de Abril");
        }

        // Configurar la acción del teclado para la búsqueda
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Realizar la búsqueda cuando se presiona la tecla Enter o se activa la acción "done" en el teclado virtual
                    search();
                    return true;
                }
                return false;
            }
        });

        // Configurar la navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_favourites) {
                startActivity(new Intent(SearchScreen.this, FavouritesScreen.class));
                return true;
            }
            if (item.getItemId() == R.id.navigation_search) {
                return true;
            }
            if (item.getItemId() == R.id.navigation_options) {
                startActivity(new Intent(SearchScreen.this, AccountScreen.class));
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
    }

    private void search() {
        String query = searchView.getText().toString();
        searchGadisProducts(query);
        searchMercadonaProducts(query);
    }

    private void searchGadisProducts(String query) {
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                "http://10.0.2.2:8000/v1/search_product1/?q=" + query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Poner títulos a los recyclerViews
                            gadisTitle.setText("GADIS");

                            // Parsear los productos del JSON
                            List<Product1Item> product1Items = parseProductos(response.getJSONArray("productos"));

                            // Configurar RecyclerView
                            Product1Adapter adapter1 = new Product1Adapter(product1Items, activity);
                            recyclerView1.setAdapter(adapter1);
                            recyclerView1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

                            adapter1.setOnItemClickListener(new Product1Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    // Obtener el objeto SearchAlbumItem correspondiente a la posición del clic
                                    Product1Item clickedItem = product1Items.get(position);
                                    // Obtener el ID del álbum seleccionado
                                    String product1Id = clickedItem.getId();
                                    // Iniciar la actividad AlbumInfoScreen y pasar el ID como extra
                                    Intent intent = new Intent(SearchScreen.this, ProductDetail1.class);
                                    intent.putExtra("PRODUCT1_ID", product1Id);
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
        });

        // Añadir la solicitud a la cola
        queue.add(request1);
    }

    private void searchMercadonaProducts(String query) {
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET,
                "http://10.0.2.2:8000/v1/search_product2/?q=" + query,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Poner títulos a los recyclerViews
                            mercadonaTitle.setText("MERCADONA");

                            // Parsear los productos del JSON
                            List<Product2Item> product2Items = parseProductos2(response.getJSONArray("productos"));

                            // Configurar RecyclerView
                            Product2Adapter adapter2 = new Product2Adapter(product2Items, activity);
                            recyclerView2.setAdapter(adapter2);
                            recyclerView2.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

                            adapter2.setOnItemClickListener(new Product2Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    // Obtener el objeto SearchAlbumItem correspondiente a la posición del clic
                                    Product2Item clickedItem = product2Items.get(position);
                                    // Obtener el ID del álbum seleccionado
                                    String product2Id = clickedItem.getId();
                                    // Iniciar la actividad AlbumInfoScreen y pasar el ID como extra
                                    Intent intent = new Intent(SearchScreen.this, ProductDetail2.class);
                                    intent.putExtra("PRODUCT2_ID", product2Id);
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
        });

        // Añadir la solicitud a la cola
        queue.add(request2);
    }

    // Método para parsear los productos del JSONArray
    private List<Product1Item> parseProductos(JSONArray jsonArray) throws JSONException {
        List<Product1Item> product1Items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String nombre = jsonObject.getString("nombre");
            String imageUrl = jsonObject.getString("imagen_url");
            Product1Item product1Item = new Product1Item(id, nombre, imageUrl);
            product1Items.add(product1Item);
        }
        return product1Items;
    }

    // Método para parsear los productos del JSONArray
    private List<Product2Item> parseProductos2(JSONArray jsonArray) throws JSONException {
        List<Product2Item> product2Items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String nombre = jsonObject.getString("nombre");
            String imageUrl = jsonObject.getString("imagen_url");
            Product2Item product2Item = new Product2Item(id, nombre, imageUrl);
            product2Items.add(product2Item);
        }
        return product2Items;
    }
}
