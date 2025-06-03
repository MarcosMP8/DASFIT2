package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dasfit.adapter.EjercicioAdapter;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Ejercicio;

import java.util.List;

public class DetalleRutinaActivity extends BaseActivity {
    private RecyclerView recyclerViewEjercicios;
    private EjercicioAdapter ejercicioAdapter;
    private GestorRutinas gestorRutinas;
    private int rutinaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_rutina);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rutinaId = getIntent().getIntExtra("rutina_id", -1);
        if (rutinaId == -1) {
            Log.e("DetalleRutinaActivity", "Error: ID de rutina no válido");
            Toast.makeText(this, getString(R.string.error_rutina_no_encontrada), Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, getString(R.string.sin_ejercicios), Toast.LENGTH_SHORT).show();
        }

        ejercicioAdapter = new EjercicioAdapter(this, listaEjercicios);
        recyclerViewEjercicios.setAdapter(ejercicioAdapter);

        Button btnAgregarEjercicio = findViewById(R.id.btnAgregarEjercicio);
        btnAgregarEjercicio.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleRutinaActivity.this, AgregarEjercicioActivity.class);
            intent.putExtra("rutina_id", rutinaId);
            startActivityForResult(intent, 1);
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            List<Ejercicio> listaEjercicios = gestorRutinas.getEjerciciosDeRutina(rutinaId);
            ejercicioAdapter = new EjercicioAdapter(this, listaEjercicios);
            recyclerViewEjercicios.setAdapter(ejercicioAdapter);
            ejercicioAdapter.notifyDataSetChanged();
        }
    }
}