package com.example.dasfit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String PREFS_NAME  = "app_prefs";
    public static final String KEY_LOCALE  = "app_locale";
    public static final String KEY_DARK    = "dark_mode";

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lang = prefs.getString(KEY_LOCALE, Locale.getDefault().getLanguage());

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = newBase.getResources().getConfiguration();
        config = new Configuration(config);
        config.setLocale(locale);

        Context contextWithLang = newBase.createConfigurationContext(config);

        super.attachBaseContext(contextWithLang);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean dark = prefs.getBoolean(KEY_DARK, false);
        AppCompatDelegate.setDefaultNightMode(
                dark
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLang = prefs.getString(KEY_LOCALE, Locale.getDefault().getLanguage());
        String currentLang = getResources()
                .getConfiguration()
                .getLocales()
                .get(0)
                .getLanguage();
        if (! savedLang.equals(currentLang)) {
            recreate();
        }
    }
}