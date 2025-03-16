package com.example.dasfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import java.util.Locale;

import com.example.dasfit.utils.NotificationHelper;
import com.example.dasfit.utils.NotificationReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistrar = findViewById(R.id.btnRegistrarEntrenamiento);
        Button btnExplorar = findViewById(R.id.btnExplorarRutinas);
        Button btnPerfil = findViewById(R.id.btnMiPerfil);

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EntrenamientosActivity.class);
            startActivity(intent);
        });

        btnExplorar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExplorarRutinasActivity.class);
            startActivity(intent);
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                solicitarPermisoAlarmas();
            }
        }
        programarNotificacion(); // Activamos la notificación programada
    }

    private void programarNotificacion() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10); // Notificación en 10 segundos para pruebas

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void solicitarPermisoAlarmas() {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
