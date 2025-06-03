package com.example.dasfit.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.dasfit.modelo.Ejercicio;
import java.util.List;

@Dao
public interface EjercicioDao {
    @Insert
    void insertarEjercicio(Ejercicio ejercicio);

    @Delete
    void eliminarEjercicio(Ejercicio ejercicio);

    @Update
    void actualizarEjercicio(Ejercicio ejercicio);

    @Query("SELECT * FROM ejercicios WHERE rutinaId = :rutinaId")
    List<Ejercicio> obtenerEjerciciosDeRutina(int rutinaId);

    @Query("SELECT * FROM ejercicios WHERE id = :id LIMIT 1")
    Ejercicio obtenerEjercicioPorId(int id);
}