package com.example.dasfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dasfit.adapter.RutinaAdapter;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Rutina;
import java.util.List;

public class EntrenamientosActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRutinas;
    private RutinaAdapter rutinaAdapter;
    private GestorRutinas gestorRutinas;
    private Button btnAgregarRutina, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamientos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerViewRutinas = findViewById(R.id.recyclerViewRutinas);
        recyclerViewRutinas.setLayoutManager(new LinearLayoutManager(this));

        btnAgregarRutina = findViewById(R.id.btnAgregarRutina);
        btnVolver = findViewById(R.id.btnVolver);

        gestorRutinas = new GestorRutinas(this);
        List<Rutina> listaRutinas = gestorRutinas.getListaRutinas();

        rutinaAdapter = new RutinaAdapter(this, listaRutinas);
        recyclerViewRutinas.setAdapter(rutinaAdapter);

        // Botón para agregar una nueva rutina
        btnAgregarRutina.setOnClickListener(v -> mostrarDialogoAgregarRutina());

        // Botón para volver atrás
        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarDialogoAgregarRutina() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Rutina");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombreRutina = input.getText().toString().trim();
            if (!nombreRutina.isEmpty()) {
                gestorRutinas.agregarRutina(new Rutina(nombreRutina));
                actualizarListaRutinas();
                Toast.makeText(this, "Rutina añadida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void actualizarListaRutinas() {
        List<Rutina> listaRutinas = gestorRutinas.getListaRutinas();
        rutinaAdapter = new RutinaAdapter(this, listaRutinas);
        recyclerViewRutinas.setAdapter(rutinaAdapter);
        rutinaAdapter.notifyDataSetChanged();
    }
}
