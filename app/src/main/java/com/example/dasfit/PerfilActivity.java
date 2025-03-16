package com.example.dasfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.example.dasfit.utils.NotificationHelper;



import com.example.dasfit.gestor.GestorRutinas;

public class PerfilActivity extends AppCompatActivity {
    private TextView tvNombrePerfil, tvEntrenamientos, tvCorreoPerfil;
    private Spinner spinnerGenero;
    private Button btnVerEntrenamientos, btnVolverPerfil;
    private ImageButton btnEditarNombre;
    private String nombreUsuario = "Tu Nombre";
    private String correoUsuario = "ejemplo@gmail.com";
    private int entrenamientosRegistrados = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicializar vistas
        tvNombrePerfil = findViewById(R.id.tvNombrePerfil);
        tvEntrenamientos = findViewById(R.id.tvEntrenamientos);
        tvCorreoPerfil = findViewById(R.id.tvCorreoPerfil);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnVerEntrenamientos = findViewById(R.id.btnVerEntrenamientos);
        btnEditarNombre = findViewById(R.id.btnEditarNombre);
        btnVolverPerfil = findViewById(R.id.btnVolverPerfil);

        // Cargar datos simulados
        tvNombrePerfil.setText(nombreUsuario);
        tvEntrenamientos.setText("Entrenamientos registrados: " + entrenamientosRegistrados);
        tvCorreoPerfil.setText(correoUsuario);

        GestorRutinas gestorRutinas = new GestorRutinas(this);
        int totalEntrenamientos = gestorRutinas.getListaRutinas().size();
        tvEntrenamientos.setText("Entrenamientos registrados: " + totalEntrenamientos);

        //Recuperar el nombre guardado
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String nombreGuardado = prefs.getString("nombre_usuario", "Tu nombre"); // Valor por defecto: "Tu nombre"
        tvNombrePerfil.setText(nombreGuardado);
        tvCorreoPerfil.setText(prefs.getString("correo", "ejemplo@gmail.com"));
        btnEditarNombre.setOnClickListener(v -> mostrarDialogoEditarPerfil());

        // Botón para ver entrenamientos registrados
        btnVerEntrenamientos.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, EntrenamientosActivity.class);
            startActivity(intent);
        });

        btnVolverPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Cierra `PerfilActivity`
        });

        // Botón para editar nombre
        btnEditarNombre.setOnClickListener(v -> {
            mostrarDialogoEditarPerfil();
        });
    }

    // Método para mostrar un diálogo y editar el nombre
    private void mostrarDialogoEditarPerfil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Perfil");

        // Crear layout para el diálogo
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Campo para el nombre
        final EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nombre");
        inputNombre.setText(tvNombrePerfil.getText().toString());
        layout.addView(inputNombre);

        // Campo para el correo
        final EditText inputCorreo = new EditText(this);
        inputCorreo.setHint("Correo");
        inputCorreo.setText(tvCorreoPerfil.getText().toString());
        layout.addView(inputCorreo);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = inputNombre.getText().toString().trim();
            String nuevoCorreo = inputCorreo.getText().toString().trim();

            if (!nuevoNombre.isEmpty() && !nuevoCorreo.isEmpty()) {
                tvNombrePerfil.setText(nuevoNombre);
                tvCorreoPerfil.setText(nuevoCorreo);

                // 🔹 Guardar en SharedPreferences
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nombre_usuario", nuevoNombre);
                editor.putString("correo", nuevoCorreo);
                editor.apply();

                // Enviar notificación
                NotificationHelper.showNotification(this, "Perfil actualizado", "Tu perfil ha sido actualizado correctamente.");
            } else {
                Toast.makeText(this, "El nombre y el correo no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


}
