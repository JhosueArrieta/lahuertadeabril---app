package com.example.lahuertadeabril;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RegisterScreen extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword, editTextBirthday;
    private Button buttonRegister;
    private Context context = this;
    private RequestQueue queue;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // Ocultamos la actionBar
        getSupportActionBar().hide();

        // Inicializar elementos de la interfaz
        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextBirthday = findViewById(R.id.FechaNacimiento);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Inicializar la cola de solicitudes Volley
        queue = Volley.newRequestQueue(this);

        // Obtener una instancia del calendario
        calendar = Calendar.getInstance();

        // Configurar el selector de fecha
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Actualizar el campo de texto con la fecha seleccionada
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                actualizarCampoFecha();
            }
        };

        // Manejar el clic del botón para mostrar el selector de fecha
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarSelectorFecha();
            }
        });

        // Manejar el clic del botón de registro
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostRequest();
            }
        });

        // Configurar cambio de foco con Enter
        setupEditorActions();
    }

    // Método para configurar los cambios de foco con Enter
    private void setupEditorActions() {
        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    editTextEmail.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    editTextPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    editTextBirthday.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextBirthday.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    buttonRegister.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    // Método para mostrar el selector de fecha
    private void mostrarSelectorFecha() {
        new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    // Método para actualizar el campo de fecha con el formato seleccionado
    private void actualizarCampoFecha() {
        String formatoFecha = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha, Locale.US);
        editTextBirthday.setText(sdf.format(calendar.getTime()));
    }

    // Método para enviar la solicitud POST (implementación no incluida en el código original)
    private void sendPostRequest() {
        Intent mainScreen = new Intent(this, MainScreen.class);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", editTextName.getText().toString());
            requestBody.put("mail", editTextEmail.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
            requestBody.put("birthdate", editTextBirthday.getText().toString());
        } catch (JSONException e) {
            Toast.makeText(context, "Error al construir la solicitud JSON", Toast.LENGTH_LONG).show();
            Log.e("RegisterScreen", "Error al construir la solicitud JSON", e);
            return;
        }

        // Agregar log para imprimir el cuerpo de la solicitud JSON antes de enviarla
        Log.d("RegisterScreen", "Request Body: " + requestBody.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/v1/users/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Usuario creado", Toast.LENGTH_SHORT).show();
                        Log.d("RegisterScreen", "Response: " + response.toString());
                        startActivity(mainScreen);
                        RegisterScreen.this.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "La conexión no se ha establecido", Toast.LENGTH_LONG).show();
                            Log.e("RegisterScreen", "No se pudo establecer la conexión", error);
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(context, "Código de respuesta " + serverCode, Toast.LENGTH_LONG).show();
                            Log.e("RegisterScreen", "Error de servidor. Código de respuesta: " + serverCode, error);
                        }
                    }
                }
        );

        this.queue.add(request);
    }
}
