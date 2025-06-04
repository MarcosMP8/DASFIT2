package com.example.dasfit;

import android.view.View;
import android.widget.TextView;

import com.example.dasfit.modelo.Gimnasio;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomInfoWindow extends InfoWindow {
    private Gimnasio gimnasioRelacionado;

    public CustomInfoWindow(MapView mapView, Gimnasio gym) {
        super(R.layout.custom_info_window, mapView);
        this.gimnasioRelacionado = gym;
    }

    @Override
    public void onOpen(Object item) {
        View v = mView;
        TextView tvNombre    = v.findViewById(R.id.tvNombreGimnasio);
        TextView tvDireccion = v.findViewById(R.id.tvDireccion);
        TextView tvHorario   = v.findViewById(R.id.tvHorario);
        TextView tvWeb       = v.findViewById(R.id.tvWeb);
        TextView tvTelefono  = v.findViewById(R.id.tvTelefono);

        // Usamos getters en lugar de campos directos:
        tvNombre.setText(gimnasioRelacionado.getNombre());
        tvDireccion.setText("Dirección: " + gimnasioRelacionado.getDireccion());
        tvHorario.setText("Horario: " + gimnasioRelacionado.getHorario());
        tvWeb.setText(gimnasioRelacionado.getWeb());
        tvTelefono.setText(gimnasioRelacionado.getTelefono());
    }

    @Override
    public void onClose() {
        // No hace nada al cerrar
    }
}