package com.example.dasfit;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dasfit.utils.NotificationReceiver;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRegistrar  = findViewById(R.id.btnRegistrarEntrenamiento);
        Button btnExplorar   = findViewById(R.id.btnExplorarRutinas);
        Button btnPerfil     = findViewById(R.id.btnMiPerfil);
        Button btnBuscarGym  = findViewById(R.id.btnBuscarGym);

        btnRegistrar.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.registrar_entrenamiento), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, EntrenamientosActivity.class));
        });

        btnExplorar.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.explorar_rutinas), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, ExplorarRutinasActivity.class));
        });

        btnPerfil.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.perfil), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, PerfilActivity.class));
        });

        btnBuscarGym.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.buscar_gimnasio), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, MapActivity.class));
        });

        // Permiso de Location en runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001
            );
        }

        // Solicita permiso de alarmas
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager am = getSystemService(AlarmManager.class);
            if (am != null && !am.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }

        // Programa notificación de prueba
        programarNotificacion();
    }

    private void programarNotificacion() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10); // Notificación en 10 segundos

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}