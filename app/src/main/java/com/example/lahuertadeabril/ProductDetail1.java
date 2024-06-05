package com.example.lahuertadeabril;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetail1 extends AppCompatActivity {

    private ImageView imageView;
    private TextView priceProduct, originProduct, nameProduct;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail1);

        // Iniciar variables
        nameProduct = findViewById(R.id.nameProduct);
        imageView = findViewById(R.id.imageView);
        priceProduct = findViewById(R.id.priceProduct);
        originProduct = findViewById(R.id.originProduct);
        progressBar = findViewById(R.id.progressBar);

        // Configurar la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.greenwater)); // Reemplaza your_custom_color con el color que desees
            actionBar.setTitle("GADIS");
        }

        // Obtener el ID del álbum desde los extras
        String product1Id = getIntent().getStringExtra("PRODUCT1_ID");

        if (product1Id == null) {
            // Manejar el caso en que albumId sea nulo
            Toast.makeText(this, "Error: product1Id es nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        //Compatibilizar con versiones anteriores
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar la navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_favourites) {
                startActivity(new Intent(ProductDetail1.this, FavouritesScreen.class));
                return true;
            }
            if (item.getItemId() == R.id.navigation_search) {
                return true;
            }
            if (item.getItemId() == R.id.navigation_options) {
                startActivity(new Intent(ProductDetail1.this, AccountScreen.class));
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        int intProduct1Id = Integer.parseInt(product1Id);
        Call<Product1Response> call = apiService.getProduct1Info(intProduct1Id);

        call.enqueue(new Callback<Product1Response>() {
            @Override
            public void onResponse(Call<Product1Response> call, Response<Product1Response> response) {
                if (response.isSuccessful() && response.body() != null){
                    Product1Response product1 = response.body();

                    // Log para el éxito de la respuesta y datos obtenidos
                    Log.d("AlbumInfoScreen", "Response received: " + product1.toString());
                    Log.d("AlbumInfoScreen", "Image URL: " + product1.getImage());

                    nameProduct.setText(product1.getName());
                    originProduct.setText(product1.getOriginProduct());
                    priceProduct.setText(String.valueOf(product1.getPriceProduct()));
                    // Cargar la imagen utilizando Picasso
                    Picasso.get().load(product1.getImage()).into(imageView);
                }else {
                    // Log para respuestas no exitosas
                    Log.e("ProductDetail1", "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Product1Response> call, Throwable t) {
                // Log para fallas en la solicitud
                Log.e("ProductDetail1", "Request failure", t);
            }
        });

        // Configuracion del PUT al clicar en el floatingactionbutton
        FloatingActionButton likeButton = findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites();
            }
        });
    }

    // Método para añadir a FavAlbum con PUT
    private void addToFavorites() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtener el id del álbum a añadir
        String product1Id = getIntent().getStringExtra("PRODUCT1_ID");

        if (product1Id == null) {
            // Manejar el caso en que albumId sea nulo
            Toast.makeText(this, "Error: product1Id es nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construir la URL del servicio REST
        String apiUrl = "http://10.0.2.2:8000/v1/add_favourites_1/" + product1Id + "/";

        // Crear un objeto JSON para la solicitud
        JSONObject jsonBody = new JSONObject();

        // Obtener el VALID_TOKEN almacenado en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);

        // Verificar si el sessionToken es nulo
        if (sessionToken == null) {
            // Manejar el caso en que sessionToken sea nulo
            Toast.makeText(this, "Error: sessionToken es nulo", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Agregar 'sessionToken' al cuerpo de la solicitud
            jsonBody.put("sessionToken", sessionToken);
        } catch (JSONException e) {
            // Manejar la excepción si ocurre algún error al agregar el sessionToken
            e.printStackTrace();
            // Mostrar mensaje de error
            Toast.makeText(this, "Error al agregar a favoritos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear la solicitud PUT con autenticación
        JsonObjectRequestWithAuthentication request = new JsonObjectRequestWithAuthentication(
                Request.Method.PUT,
                apiUrl,
                jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        // Mensaje de verificación si se lleva a cabo o no el PUT
                        Toast.makeText(ProductDetail1.this, "Añadido a favoritos exitosamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        // Mensaje de error si no se lleva a cabo la solicitud
                        Log.e("AlbumInfoScreen", "Error al añadir a favoritos", error);
                        Toast.makeText(ProductDetail1.this, "Error al añadir a favoritos", Toast.LENGTH_SHORT).show();
                    }
                },
                this  // Contexto para obtener el sessionToken
        );

        // Obtener la cola de solicitudes de Volley y agregar la solicitud a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}