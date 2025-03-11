package com.example.dasfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dasfit.DetalleRutinaActivity;
import com.example.dasfit.R;
import com.example.dasfit.modelo.Rutina;
import java.util.List;

public class RutinaAdapter extends RecyclerView.Adapter<RutinaAdapter.ViewHolder> {
    private List<Rutina> listaRutinas;
    private Context context;

    public RutinaAdapter(Context context, List<Rutina> listaRutinas) {
        this.context = context;
        this.listaRutinas = listaRutinas;
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

        // Hacer que solo el botón "ojo" abra la rutina
        holder.btnVerRutina.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleRutinaActivity.class);
            intent.putExtra("rutina_id", rutina.getId());
            context.startActivity(intent);
        });
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
