package com.example.dasfit.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "rutinas")
public class Rutina {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;

    @Ignore
    private List<Ejercicio> ejercicios; // Ignoramos porque Room no maneja listas directamente

    public Rutina(String nombre) {
        this.nombre = nombre;
        this.ejercicios = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
