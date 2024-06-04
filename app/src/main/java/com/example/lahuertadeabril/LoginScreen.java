package com.example.lahuertadeabril;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreen extends AppCompatActivity {

    private Context context = this;
    private RequestQueue queue;
    private Button loginButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Ocultamos la actionBar
        getSupportActionBar().hide();

        // Inicializar variables
        queue = Volley.newRequestQueue(this);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);

        // Manejamos acción al pulsar el botón
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Manejamos la acción del teclado en el campo de contraseña
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    loginUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void loginUser() {
        // Desactivar el botón para evitar múltiples pulsaciones
        loginButton.setEnabled(false);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", editTextEmail.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
        } catch (JSONException e) {
            Toast.makeText(context, "Error al construir el JSON", Toast.LENGTH_SHORT).show();
            loginButton.setEnabled(true);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/v1/sessions/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        String receivedToken;
                        try {
                            receivedToken = response.getString("sessionToken");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VALID_USERNAME", editTextEmail.getText().toString());
                        editor.putString("VALID_TOKEN", receivedToken);
                        editor.apply();

                        loginButton.setEnabled(true);
                        startActivity(new Intent(LoginScreen.this, SearchScreen.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        int serverCodeError = error.networkResponse != null ? error.networkResponse.statusCode : -1;
                        String errorMessage = error.networkResponse != null ? new String(error.networkResponse.data) : "Unknown error";
                        Toast.makeText(context, "Código de respuesta: " + serverCodeError + "\nMensaje: " + errorMessage, Toast.LENGTH_LONG).show();
                        loginButton.setEnabled(true);
                    }
                }
        );
        queue.add(request);
    }
}
