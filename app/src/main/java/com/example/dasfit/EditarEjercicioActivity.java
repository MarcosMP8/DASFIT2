package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Ejercicio;

public class EditarEjercicioActivity extends AppCompatActivity {
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
        gestorRutinas = new GestorRutinas(this);
        ejercicio = gestorRutinas.obtenerEjercicioPorId(ejercicioId);

        etNombre = findViewById(R.id.etNombreEjercicio);
        etRepeticiones = findViewById(R.id.etRepeticiones);
        etPeso = findViewById(R.id.etPeso);
        etDuracion = findViewById(R.id.etDuracion);
        btnActualizar = findViewById(R.id.btnActualizarEjercicio);

        if (ejercicio != null) {
            etNombre.setText(ejercicio.getNombre());
            etRepeticiones.setText(String.valueOf(ejercicio.getRepeticiones()));
            etPeso.setText(String.valueOf(ejercicio.getPeso()));
            etDuracion.setText(String.valueOf(ejercicio.getDuracion()));
        }

        btnActualizar.setOnClickListener(v -> {
            ejercicio.setNombre(etNombre.getText().toString().trim());
            ejercicio.setRepeticiones(Integer.parseInt(etRepeticiones.getText().toString()));
            ejercicio.setPeso(Double.parseDouble(etPeso.getText().toString()));
            ejercicio.setDuracion(Integer.parseInt(etDuracion.getText().toString()));

            gestorRutinas.actualizarEjercicio(ejercicio);
            Toast.makeText(this, "Ejercicio actualizado", Toast.LENGTH_SHORT).show();

            // 🔹 Devolver resultado a DetalleRutinaActivity
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);

            finish();
        });

    }
}
