package com.example.dasfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dasfit.gestor.GestorRutinas;

public class PerfilActivity extends AppCompatActivity {
    private TextView tvNombrePerfil, tvEntrenamientos;
    private Spinner spinnerGenero;
    private Button btnVerEntrenamientos, btnVolverPerfil;
    private ImageButton btnEditarNombre, btnAjustes;
    private String nombreUsuario = "Tu Nombre"; // Simulación de datos temporales
    private int entrenamientosRegistrados = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar vistas
        tvNombrePerfil = findViewById(R.id.tvNombrePerfil);
        tvEntrenamientos = findViewById(R.id.tvEntrenamientos);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnVerEntrenamientos = findViewById(R.id.btnVerEntrenamientos);
        btnEditarNombre = findViewById(R.id.btnEditarNombre);
        btnAjustes = findViewById(R.id.btnAjustes);
        btnVolverPerfil = findViewById(R.id.btnVolverPerfil);

        // Cargar datos simulados
        tvNombrePerfil.setText(nombreUsuario);
        tvEntrenamientos.setText("Entrenamientos registrados: " + entrenamientosRegistrados);

        GestorRutinas gestorRutinas = new GestorRutinas(this);
        int totalEntrenamientos = gestorRutinas.getListaRutinas().size();
        tvEntrenamientos.setText("Entrenamientos registrados: " + totalEntrenamientos);

        // Botón para editar nombre
        btnEditarNombre.setOnClickListener(v -> mostrarDialogoEditarNombre());

        // Botón para ver entrenamientos registrados
        btnVerEntrenamientos.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, EntrenamientosActivity.class);
            startActivity(intent);
        });

        // Botón de ajustes (implementaremos cambio de idioma después)
        btnAjustes.setOnClickListener(v -> {
            // Aquí podemos abrir un menú para cambiar el idioma
        });

        btnVolverPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Cierra `PerfilActivity`
        });

    }

    // Método para mostrar un diálogo y editar el nombre
    private void mostrarDialogoEditarNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Nombre");

        final EditText input = new EditText(this);
        input.setText(nombreUsuario);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            nombreUsuario = input.getText().toString().trim();
            tvNombrePerfil.setText(nombreUsuario);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
