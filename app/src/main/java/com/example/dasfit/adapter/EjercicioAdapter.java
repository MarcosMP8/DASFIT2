package com.example.dasfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dasfit.DetalleRutinaActivity;
import com.example.dasfit.EditarEjercicioActivity;
import com.example.dasfit.R;
import com.example.dasfit.gestor.GestorRutinas;
import com.example.dasfit.modelo.Ejercicio;
import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder> {
    private List<Ejercicio> listaEjercicios;
    private Context context;
    private GestorRutinas gestorRutinas;

    public EjercicioAdapter(Context context, List<Ejercicio> listaEjercicios) {
        this.context = context;
        this.listaEjercicios = listaEjercicios;
        this.gestorRutinas = new GestorRutinas(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ejercicio ejercicio = listaEjercicios.get(position);
        holder.tvNombreEjercicio.setText(ejercicio.getNombre());
        holder.tvDetallesEjercicio.setText(ejercicio.getRepeticiones() + " reps | " +
                ejercicio.getPeso() + " kg | " + ejercicio.getDuracion() + " seg");

        // Botón para editar ejercicio
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarEjercicioActivity.class);
            intent.putExtra("ejercicio_id", ejercicio.getId());
            ((DetalleRutinaActivity) context).startActivityForResult(intent, 2);
        });

        // Botón para eliminar ejercicio
        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.eliminar_ejercicio))
                    .setMessage(context.getString(R.string.eliminar_ejercicio_confirmacion))
                    .setPositiveButton(context.getString(R.string.si), (dialog, which) -> {
                        gestorRutinas.eliminarEjercicio(ejercicio);
                        listaEjercicios.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context,context.getString(R.string.ejercicio_eliminado), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(context.getString(R.string.cancelar), null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaEjercicios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreEjercicio, tvDetallesEjercicio;
        ImageButton btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombreEjercicio = itemView.findViewById(R.id.tvNombreEjercicio);
            tvDetallesEjercicio = itemView.findViewById(R.id.tvDetallesEjercicio);
            btnEditar = itemView.findViewById(R.id.btnEditarEjercicio);
            btnEliminar = itemView.findViewById(R.id.btnEliminarEjercicio);
        }
    }
}
