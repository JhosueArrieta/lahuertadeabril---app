package com.example.lahuertadeabril;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordScreen extends AppCompatActivity {

    private EditText etCurrentPassword, etNewPassword;
    private Button buttonConfirmPassword;
    private ProgressBar progressBar;
    private RequestQueue queue;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);

        // Ocultamos la actionBar
        getSupportActionBar().hide();

        etCurrentPassword = findViewById(R.id.currentPassword);
        etNewPassword = findViewById(R.id.newPassword);
        buttonConfirmPassword = findViewById(R.id.buttonChangePassword);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar la cola de solicitudes Volley
        queue = Volley.newRequestQueue(this);

        buttonConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword();
            }
        });
    }

    private void newPassword() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtener el token de sesión de SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString("VALID_TOKEN", "");

        // Construir el cuerpo JSON de la solicitud
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("current_password", etCurrentPassword.getText().toString());
            requestBody.put("new_password", etNewPassword.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Crear la solicitud POST con el token de sesión en el encabezado
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/v1/password/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ChangePasswordScreen.this, AccountScreen.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        int statusCode = error.networkResponse != null ? error.networkResponse.statusCode : -1;
                        String message = error.getMessage() != null ? error.getMessage() : "Error desconocido";
                        Toast.makeText(context, "Error " + statusCode + ": " + message, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("sessionToken", sessionToken);
                return headers;
            }
        };

        queue.add(request);
    }


}
