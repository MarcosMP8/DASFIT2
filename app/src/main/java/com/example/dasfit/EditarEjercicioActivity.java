package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Ejercicio;

public class EditarEjercicioActivity extends BaseActivity {
    private EditText etNombre, etRepeticiones, etPeso, etDuracion;
    private Button btnActualizar;
    private GestorRutinas gestorRutinas;
    private Ejercicio ejercicio;
    private int ejercicioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ejercicio);

        ejercicioId = getIntent().getIntExtra("ejercicio_id", -1);
        if (ejercicioId == -1) {
            Toast.makeText(this, (R.string.error_obtener_ejercicio), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        gestorRutinas = new GestorRutinas(this);
        ejercicio = gestorRutinas.obtenerEjercicioPorId(ejercicioId);

        if (ejercicio == null) {
            Toast.makeText(this, getString(R.string.error_ejercicio_no_encontrado), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        etNombre = findViewById(R.id.etNombre);
        etRepeticiones = findViewById(R.id.etRepeticiones);
        etPeso = findViewById(R.id.etPeso);
        etDuracion = findViewById(R.id.etDuracion);
        btnActualizar = findViewById(R.id.btnActualizarEjercicio);

        etNombre.setText(ejercicio.getNombre());
        etRepeticiones.setText(String.valueOf(ejercicio.getRepeticiones()));
        etPeso.setText(String.valueOf(ejercicio.getPeso()));
        etDuracion.setText(String.valueOf(ejercicio.getDuracion()));

        btnActualizar.setOnClickListener(v -> {
            ejercicio.setNombre(etNombre.getText().toString().trim());
            ejercicio.setRepeticiones(Integer.parseInt(etRepeticiones.getText().toString()));
            ejercicio.setPeso(Double.parseDouble(etPeso.getText().toString()));
            ejercicio.setDuracion(Integer.parseInt(etDuracion.getText().toString()));

            gestorRutinas.actualizarEjercicio(ejercicio);
            Toast.makeText(this, getString(R.string.ejercicio_actualizado), Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }
}