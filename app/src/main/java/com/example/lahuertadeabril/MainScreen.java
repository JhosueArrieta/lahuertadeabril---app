package com.example.lahuertadeabril;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lahuertadeabril.R;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Ocultamos la actionBar
        getSupportActionBar().hide();

        // Referenciar los botones
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        // Configurar OnClickListener para el botón de Iniciar sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar LoginActivity
                Intent loginIntent = new Intent(MainScreen.this, LoginScreen.class);
                startActivity(loginIntent);
            }
        });

        // Configurar OnClickListener para el botón de Registrarse
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar RegisterActivity
                Intent registerIntent = new Intent(MainScreen.this, RegisterScreen.class);
                startActivity(registerIntent);
            }
        });
    }
}