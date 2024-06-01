package com.example.lahuertadeabril;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountScreen extends AppCompatActivity {
    private Context context = this;
    private TextView correo;
    private ProgressBar progressBar;
    private RequestQueue queue;
    private Button deleteButton;
    private TextView changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);

        // Inicializar vistas
        progressBar = findViewById(R.id.progressBar);
        queue = Volley.newRequestQueue(this);
        deleteButton = findViewById(R.id.deleteButton);
        correo = findViewById(R.id.correo);
        changePassword = findViewById(R.id.passwordReset);

        // Configurar la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.greenwater)); // Reemplaza your_custom_color con el color que desees
            actionBar.setTitle("La Huerta de Abril");
        }

        // Configurar compatibilidad con versiones anteriores
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar el botón de cerrar sesión
        deleteButton.setOnClickListener(view -> deleteUser());
        // Configurar cambiar contraseña
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountScreen.this, ChangePasswordScreen.class));
                finish();
            }
        });

        // Configurar la navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_favourites) {
                startActivity(new Intent(AccountScreen.this, FavouritesScreen.class));
                return true;
            }
            if (item.getItemId() == R.id.navigation_search) {
                startActivity(new Intent(AccountScreen.this, SearchScreen.class));
                return true;
            }
            if (item.getItemId() == R.id.navigation_options) {
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_options);

        // Obtener y mostrar el correo del usuario
        account();
    }

    // Método para mostrar el correo del usuario
    private void account() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtener el token de sesión de SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString("VALID_TOKEN", "");

        // Crear una solicitud GET con Volley para obtener información de la cuenta
        String url1 = Server.name + "/v1/account/";
        JsonObjectRequestWithAuthentication request1 = new JsonObjectRequestWithAuthentication(
                Request.Method.GET,
                url1,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            String email = response.getString("correo");
                            // Actualiza el TextView con el email obtenido
                            correo.setText(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                },
                context
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("sessionToken", sessionToken);
                return headers;
            }
        };

        // Agregar la petición a la cola de solicitudes
        this.queue.add(request1);
    }

    // Método para cerrar sesión del usuario
    private void deleteUser() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtener el token de sesión de SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString("VALID_TOKEN", "");

        // Crear una solicitud DELETE con Volley para cerrar sesión y borrar el token de sesión
        String url = Server.name + "/v1/sessions/";
        JsonObjectRequestWithAuthentication request2 = new JsonObjectRequestWithAuthentication(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);

                        // Borrar el token de sesión almacenado en SharedPreferences
                        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("VALID_TOKEN");
                        editor.apply();

                        // Redirigir la aplicación a la pantalla principal después de cerrar sesión
                        startActivity(new Intent(AccountScreen.this, MainScreen.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
                    }
                },
                context
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("sessionToken", sessionToken);
                return headers;
            }
        };

        // Agregar la petición a la cola de solicitudes
        this.queue.add(request2);
    }
}
