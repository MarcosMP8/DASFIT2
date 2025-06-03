package com.example.dasfit;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.dasfit.R;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class AjustesActivity extends BaseActivity {

    private Spinner spinnerLanguage;
    private SwitchCompat switchTheme;
    private MaterialButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        spinnerLanguage = findViewById(R.id.spinnerIdioma);
        switchTheme     = findViewById(R.id.switchTema);
        btnBack         = findViewById(R.id.btnBackAjustes);

        setupLanguageSpinner();
        setupThemeSwitch();
        setupBackButton();
    }

    private void setupLanguageSpinner() {
        String[] langs = getResources().getStringArray(R.array.languages);
        String spanish = langs[0];
        String english = langs[1];
        String basque  = langs[2];

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLoc = prefs.getString(KEY_LOCALE, Locale.getDefault().getLanguage());
        if ("en".equals(savedLoc)) {
            spinnerLanguage.setSelection(adapter.getPosition(english));
        } else if ("eu".equals(savedLoc)) {
            spinnerLanguage.setSelection(adapter.getPosition(basque));
        } else {
            spinnerLanguage.setSelection(adapter.getPosition(spanish));
        }

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String sel = parent.getItemAtPosition(pos).toString();
                String code;
                if (sel.equals(english)) {
                    code = "en";
                } else if (sel.equals(basque)) {
                    code = "eu";
                } else {
                    code = "es";
                }

                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                String current = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .getString(KEY_LOCALE, "");
                if (!code.equals(current)) {
                    editor.putString(KEY_LOCALE, code);
                    editor.apply();
                    recreate();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setupThemeSwitch() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean dark = prefs.getBoolean(KEY_DARK, false);
        switchTheme.setChecked(dark);

        switchTheme.setOnCheckedChangeListener((button, isChecked) -> {
            SharedPreferences.Editor edit = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            edit.putBoolean(KEY_DARK, isChecked).apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }
}