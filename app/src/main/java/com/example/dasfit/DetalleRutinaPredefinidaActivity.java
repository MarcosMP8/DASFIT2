package com.example.dasfit;

import android.os.Bundle;
import android.util.Log;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DetalleRutinaPredefinidaActivity extends BaseActivity {
    private TextView tvDetallesRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_rutina_predefinida);

        TextView tvTituloRutina = findViewById(R.id.tvTituloRutina);
        TextView tvDetallesRutina = findViewById(R.id.tvDetallesRutina);
        String nombreRutina = getIntent().getStringExtra("nombre_rutina");

        if (nombreRutina != null) {
            SpannableString textoSubrayado = new SpannableString(nombreRutina);
            textoSubrayado.setSpan(new UnderlineSpan(), 0, nombreRutina.length(), 0);
            tvTituloRutina.setText(textoSubrayado);

            tvDetallesRutina.setText(cargarDetallesRutina(nombreRutina));
        }

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private String cargarDetallesRutina(String nombreRutina) {
        StringBuilder detalles = new StringBuilder();
        String nombreArchivo = nombreRutina.replace(" ", "") + ".txt";

        try {
            InputStream inputStream = getAssets().open(nombreArchivo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                detalles.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.e("DetalleRutina", "Error al leer el archivo " + nombreArchivo, e);
            return getString(R.string.error_cargar_rutina);
        }
        return detalles.toString();
    }
}