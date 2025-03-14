package com.example.dasfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroupIdioma, radioGroupTema;
    private Button btnGuardarAjustes, btnVolver;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar las preferencias ANTES de aplicar el tema
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean esModoOscuro = prefs.getBoolean("modo_oscuro", false);
        AppCompatDelegate.setDefaultNightMode(esModoOscuro ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_settings);

        // Inicializar elementos
        radioGroupIdioma = findViewById(R.id.radioGroupIdioma);
        radioGroupTema = findViewById(R.id.radioGroupTema);
        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes);
        btnVolver = findViewById(R.id.btnVolver);

        // Cargar las opciones guardadas
        cargarPreferencias();

        // Botones
        btnGuardarAjustes.setOnClickListener(v -> guardarAjustes());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarPreferencias() {
        String idioma = prefs.getString("idioma", "es");
        boolean esModoOscuro = prefs.getBoolean("modo_oscuro", false);

        if ("es".equals(idioma)) {
            radioGroupIdioma.check(R.id.radioEspanol);
        } else {
            radioGroupIdioma.check(R.id.radioIngles);
        }

        if (esModoOscuro) {
            radioGroupTema.check(R.id.radioOscuro);
        } else {
            radioGroupTema.check(R.id.radioClaro);
        }
    }

    private void guardarAjustes() {
        String idiomaSeleccionado = (radioGroupIdioma.getCheckedRadioButtonId() == R.id.radioEspanol) ? "es" : "en";
        boolean modoOscuroSeleccionado = (radioGroupTema.getCheckedRadioButtonId() == R.id.radioOscuro);

        // Guardar en `SharedPreferences`
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("idioma", idiomaSeleccionado);
        editor.putBoolean("modo_oscuro", modoOscuroSeleccionado);
        editor.apply();

        // Reiniciar completamente la app para aplicar los cambios
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}
