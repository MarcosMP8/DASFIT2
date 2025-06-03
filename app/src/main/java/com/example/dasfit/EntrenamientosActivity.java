package com.example.dasfit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dasfit.adapter.RutinaAdapter;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Rutina;

import java.util.List;

public class EntrenamientosActivity extends BaseActivity {
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

        btnAgregarRutina.setOnClickListener(v -> mostrarDialogoAgregarRutina());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarDialogoAgregarRutina() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.nueva_rutina));

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.guardar_rutina), (dialog, which) -> {
            String nombreRutina = input.getText().toString().trim();
            if (!nombreRutina.isEmpty()) {
                gestorRutinas.agregarRutina(new Rutina(nombreRutina));
                actualizarListaRutinas();
                Toast.makeText(this, getString(R.string.rutina_anadida), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.nombre_vacio), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void actualizarListaRutinas() {
        List<Rutina> listaRutinas = gestorRutinas.getListaRutinas();
        rutinaAdapter = new RutinaAdapter(this, listaRutinas);
        recyclerViewRutinas.setAdapter(rutinaAdapter);
        rutinaAdapter.notifyDataSetChanged();
    }
}