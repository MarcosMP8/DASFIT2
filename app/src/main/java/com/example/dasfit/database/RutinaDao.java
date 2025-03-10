package com.example.dasfit.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.dasfit.modelo.Rutina;
import java.util.List;

@Dao
public interface RutinaDao {
    @Insert
    void insertarRutina(Rutina rutina);

    @Update
    void actualizarRutina(Rutina rutina);

    @Delete
    void eliminarRutina(Rutina rutina);

    @Query("SELECT * FROM rutinas")
    List<Rutina> obtenerTodasRutinas();
}
