package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistrar = findViewById(R.id.btnRegistrarEntrenamiento);
        Button btnExplorar = findViewById(R.id.btnExplorarRutinas);
        Button btnPerfil = findViewById(R.id.btnMiPerfil);

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EntrenamientosActivity.class);
            startActivity(intent);
        });

        btnExplorar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class); // O ajusta la actividad correcta
            startActivity(intent);
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });
    }
}