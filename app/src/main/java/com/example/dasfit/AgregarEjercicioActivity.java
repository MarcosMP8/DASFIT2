package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Ejercicio;

public class AgregarEjercicioActivity extends BaseActivity {
    private EditText etNombreEjercicio, etRepeticiones, etPeso, etDuracion;
    private Button btnGuardarEjercicio;
    private GestorRutinas gestorRutinas;
    private int rutinaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ejercicio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rutinaId = getIntent().getIntExtra("rutina_id", -1);
        if (rutinaId == -1) {
            Toast.makeText(this, getString(R.string.error_rutina_no_encontrada), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        etNombreEjercicio = findViewById(R.id.etNombreEjercicio);
        etRepeticiones = findViewById(R.id.etRepeticiones);
        etPeso = findViewById(R.id.etPeso);
        etDuracion = findViewById(R.id.etDuracion);
        btnGuardarEjercicio = findViewById(R.id.btnGuardarEjercicio);

        gestorRutinas = new GestorRutinas(this);

        btnGuardarEjercicio.setOnClickListener(v -> {
            String nombre = etNombreEjercicio.getText().toString().trim();
            String repeticionesStr = etRepeticiones.getText().toString().trim();
            String pesoStr = etPeso.getText().toString().trim();
            String duracionStr = etDuracion.getText().toString().trim();

            if (nombre.isEmpty() || repeticionesStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.nombre_repeticiones_obligatorios), Toast.LENGTH_LONG).show();
                return;
            }

            int repeticiones = Integer.parseInt(repeticionesStr);
            double peso = pesoStr.isEmpty() ? 0 : Double.parseDouble(pesoStr);
            int duracion = duracionStr.isEmpty() ? 0 : Integer.parseInt(duracionStr);

            Ejercicio nuevoEjercicio = new Ejercicio(rutinaId, nombre, repeticiones, peso, duracion);
            gestorRutinas.agregarEjercicio(nuevoEjercicio);

            Toast.makeText(this, getString(R.string.ejercicio_agregado), Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK, new Intent());
            finish();
        });
    }
}