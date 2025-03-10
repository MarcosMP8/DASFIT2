package com.example.dasfit;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dasfit.adapter.EjercicioAdapter;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Ejercicio;
import java.util.List;

public class DetalleRutinaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewEjercicios;
    private EjercicioAdapter ejercicioAdapter;
    private GestorRutinas gestorRutinas;
    private int rutinaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_rutina);

        // Configurar Toolbar con botón de volver atrás
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_media_previous);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // Botón de retroceso

        rutinaId = getIntent().getIntExtra("rutina_id", -1);
        if (rutinaId == -1) {
            Log.e("DetalleRutinaActivity", "Error: ID de rutina no válido");
            Toast.makeText(this, "Error: Rutina no encontrada", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d("DetalleRutinaActivity", "Cargando rutina con ID: " + rutinaId);

        gestorRutinas = new GestorRutinas(this);
        recyclerViewEjercicios = findViewById(R.id.recyclerViewEjercicios);
        recyclerViewEjercicios.setLayoutManager(new LinearLayoutManager(this));

        List<Ejercicio> listaEjercicios = gestorRutinas.getEjerciciosDeRutina(rutinaId);

        if (listaEjercicios.isEmpty()) {
            Log.d("DetalleRutinaActivity", "No hay ejercicios para esta rutina");
            Toast.makeText(this, "No hay ejercicios en esta rutina", Toast.LENGTH_SHORT).show();
        }

        ejercicioAdapter = new EjercicioAdapter(listaEjercicios);
        recyclerViewEjercicios.setAdapter(ejercicioAdapter);

        // Botón para volver a la pantalla anterior
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Regresa a la actividad anterior
    }
}
