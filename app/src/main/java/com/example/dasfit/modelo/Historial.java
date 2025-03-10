package com.example.dasfit.modelo;

public class Historial {
    private String fecha;
    private String tipoActividad; // "gimnasio", "correr", "bicicleta"
    private int duracion;

    public Historial(String fecha, String tipoActividad, int duracion) {
        this.fecha = fecha;
        this.tipoActividad = tipoActividad;
        this.duracion = duracion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public int getDuracion() {
        return duracion;
    }
}