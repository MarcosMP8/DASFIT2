package com.example.dasfit.modelo;

public class Gimnasio {
    private String nombre;
    private double latitud;
    private double longitud;
    private String direccion;
    private String horario;
    private String web;
    private String telefono;
    private String tipo;

    public Gimnasio(String nombre, double lat, double lon,
                    String direccion, String horario,
                    String web, String telefono, String tipo) {
        this.nombre = nombre;
        this.latitud = lat;
        this.longitud = lon;
        this.direccion = direccion;
        this.horario = horario;
        this.web = web;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    // Getters para cada campo:
    public String getNombre() {
        return nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getHorario() {
        return horario;
    }

    public String getWeb() {
        return web;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getTipo() {
        return tipo;
    }
}