package com.example.dasfit.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "ejercicios",
        foreignKeys = @ForeignKey(entity = Rutina.class,
                parentColumns = "id",
                childColumns = "rutinaId",
                onDelete = ForeignKey.CASCADE))
public class Ejercicio {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int rutinaId;
    private String nombre;
    private int repeticiones;
    private double peso;
    private int duracion;

    public Ejercicio(int rutinaId, String nombre, int repeticiones, double peso, int duracion) {
        this.rutinaId = rutinaId;
        this.nombre = nombre;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRutinaId() {
        return rutinaId;
    }

    public void setRutinaId(int rutinaId) {
        this.rutinaId = rutinaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRepeticiones() {
        return repeticiones;
    }
    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
