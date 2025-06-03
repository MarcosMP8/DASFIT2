package com.example.dasfit.gestor;

import android.content.Context;
import com.example.dasfit.database.AppDatabase;
import com.example.dasfit.database.RutinaDao;
import com.example.dasfit.database.EjercicioDao;
import com.example.dasfit.modelo.Rutina;
import com.example.dasfit.modelo.Ejercicio;
import java.util.List;

public class GestorRutinas {
    private RutinaDao rutinaDao;
    private EjercicioDao ejercicioDao;

    public GestorRutinas(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.rutinaDao = db.rutinaDao();
        this.ejercicioDao = db.ejercicioDao();
    }

    public void agregarRutina(Rutina rutina) {
        rutinaDao.insertarRutina(rutina);
    }

    public List<Ejercicio> obtenerEjerciciosDeRutina(int rutinaId) {
        return ejercicioDao.obtenerEjerciciosDeRutina(rutinaId);
    }

    public void eliminarRutina(Rutina rutina) {
        if (rutina == null) return;

        Rutina rutinaBD = rutinaDao.obtenerRutinaPorId(rutina.getId());
        if (rutinaBD == null) return;

        List<Ejercicio> ejercicios = ejercicioDao.obtenerEjerciciosDeRutina(rutina.getId());
        if (ejercicios != null && !ejercicios.isEmpty()) {
            for (Ejercicio ejercicio : ejercicios) {
                ejercicioDao.eliminarEjercicio(ejercicio);
            }
        }

        rutinaDao.eliminarRutina(rutinaBD);
    }

    public List<Rutina> getListaRutinas() {
        return rutinaDao.obtenerTodasRutinas();
    }

    public void agregarEjercicio(Ejercicio ejercicio) {
        ejercicioDao.insertarEjercicio(ejercicio);
    }

    public List<Ejercicio> getEjerciciosDeRutina(int rutinaId) {
        return ejercicioDao.obtenerEjerciciosDeRutina(rutinaId);
    }

    // Método para eliminar un ejercicio de la base de datos
    public void eliminarEjercicio(Ejercicio ejercicio) {
        ejercicioDao.eliminarEjercicio(ejercicio);
    }

    // Método para actualizar un ejercicio en la base de datos
    public void actualizarEjercicio(Ejercicio ejercicio) {
        ejercicioDao.actualizarEjercicio(ejercicio);
    }

    // Método para obtener un ejercicio por ID
    public Ejercicio obtenerEjercicioPorId(int id) {
        return ejercicioDao.obtenerEjercicioPorId(id);
    }
}