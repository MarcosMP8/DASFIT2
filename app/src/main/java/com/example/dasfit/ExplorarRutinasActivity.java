package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.dasfit.adapter.RutinaPredefinidaAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExplorarRutinasActivity extends BaseActivity {
    private RecyclerView recyclerViewRutinas;
    private RutinaPredefinidaAdapter rutinaAdapter;
    private List<String> listaRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar_rutinas);

        recyclerViewRutinas = findViewById(R.id.recyclerViewRutinas);
        recyclerViewRutinas.setLayoutManager(new LinearLayoutManager(this));

        listaRutinas = cargarRutinasDesdeArchivo();
        rutinaAdapter = new RutinaPredefinidaAdapter(listaRutinas, this::abrirDetalleRutina);
        recyclerViewRutinas.setAdapter(rutinaAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private List<String> cargarRutinasDesdeArchivo() {
        List<String> titulosRutinas = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("rutinas.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    titulosRutinas.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e("ExplorarRutinas", "Error al leer el archivo", e);
        }
        return titulosRutinas;
    }

    private void abrirDetalleRutina(String nombreRutina) {
        Intent intent = new Intent(this, DetalleRutinaPredefinidaActivity.class);
        intent.putExtra("nombre_rutina", nombreRutina);
        startActivity(intent);
    }
}