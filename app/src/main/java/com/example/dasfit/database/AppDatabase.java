package com.example.dasfit.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.dasfit.modelo.Rutina;
import com.example.dasfit.modelo.Ejercicio;

@Database(entities = {Rutina.class, Ejercicio.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instancia;

    public abstract RutinaDao rutinaDao();
    public abstract EjercicioDao ejercicioDao();

    public static AppDatabase getDatabase(final Context context) {
        if (instancia == null) {
            synchronized (AppDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "rutinas_db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instancia;
    }
}