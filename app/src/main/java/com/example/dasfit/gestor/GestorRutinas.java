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

    public void eliminarRutina(Rutina rutina) {
        rutinaDao.eliminarRutina(rutina);
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
}
