package com.example.dasfit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dasfit.R;
import com.example.dasfit.modelo.Rutina;
import java.util.List;

public class RutinaAdapter extends RecyclerView.Adapter<RutinaAdapter.ViewHolder> {
    private List<Rutina> listaRutinas;
    private OnItemClickListener listener; // Nuevo Listener para clics

    public interface OnItemClickListener {
        void onItemClick(Rutina rutina);
    }

    public RutinaAdapter(List<Rutina> listaRutinas, OnItemClickListener listener) {
        this.listaRutinas = listaRutinas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rutina, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rutina rutina = listaRutinas.get(position);
        holder.tvNombreRutina.setText(rutina.getNombre());

        // Evento de clic en cada rutina
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(rutina);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRutinas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreRutina;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombreRutina = itemView.findViewById(R.id.tvNombreRutina);
        }
    }
}
