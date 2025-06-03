package com.example.dasfit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.dasfit.R;

public class RutinaPredefinidaAdapter extends RecyclerView.Adapter<RutinaPredefinidaAdapter.ViewHolder> {
    private List<String> listaRutinas;
    private OnRutinaClickListener rutinaClickListener;

    public interface OnRutinaClickListener {
        void onRutinaClick(String nombreRutina);
    }

    public RutinaPredefinidaAdapter(List<String> listaRutinas, OnRutinaClickListener listener) {
        this.listaRutinas = listaRutinas;
        this.rutinaClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rutina_predefinida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nombreRutina = listaRutinas.get(position);
        holder.tvNombreRutina.setText(nombreRutina);
        holder.btnVerRutina.setOnClickListener(v -> rutinaClickListener.onRutinaClick(nombreRutina));
    }

    @Override
    public int getItemCount() {
        return listaRutinas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreRutina;
        ImageButton btnVerRutina;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombreRutina = itemView.findViewById(R.id.tvNombreRutina);
            btnVerRutina = itemView.findViewById(R.id.btnVerRutina);
        }
    }
}