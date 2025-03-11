package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dasfit.adapter.RutinaAdapter;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Rutina;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewRutinas;
    private RutinaAdapter rutinaAdapter;
    private GestorRutinas gestorRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewRutinas = findViewById(R.id.recyclerViewRutinas);
        recyclerViewRutinas.setLayoutManager(new LinearLayoutManager(this));

        gestorRutinas = new GestorRutinas(this);
        List<Rutina> listaRutinas = gestorRutinas.getListaRutinas();

        if (listaRutinas.isEmpty()) {
            gestorRutinas.agregarRutina(new Rutina("Rutina de Fuerza"));
            gestorRutinas.agregarRutina(new Rutina("Cardio Intensivo"));
            gestorRutinas.agregarRutina(new Rutina("Full Body Express"));
            listaRutinas = gestorRutinas.getListaRutinas();
        }

        rutinaAdapter = new RutinaAdapter(this, listaRutinas);
        recyclerViewRutinas.setAdapter(rutinaAdapter);
    }
}
