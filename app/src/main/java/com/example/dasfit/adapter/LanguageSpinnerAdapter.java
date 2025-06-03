package com.example.dasfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dasfit.R;

public class LanguageSpinnerAdapter extends ArrayAdapter<String> {

    // Dibujables de banderas, en el mismo orden que el array de cadenas
    private final Integer[] flagRes = {
            R.drawable.flag_es,  // Español
            R.drawable.flag_en,  // English (puede ser flag_us o flag_gb)
            R.drawable.flag_eu   // Euskara (Ikurriña)
    };

    private final String[] labels;

    public LanguageSpinnerAdapter(Context ctx, String[] labels) {
        super(ctx, R.layout.spinner_item_language, labels);
        this.labels = labels;
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        View item = (convertView != null)
                ? convertView
                : LayoutInflater.from(getContext())
                .inflate(R.layout.spinner_item_language, parent, false);

        TextView tv = item.findViewById(R.id.tvLanguageName);
        ImageView iv = item.findViewById(R.id.imgFlag);

        tv.setText(labels[position]);
        // Solo mostramos la bandera si existe un recurso válido en flagRes
        if (flagRes[position] != null) {
            iv.setImageResource(flagRes[position]);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }

        return item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Vista “cerrada” (el Spinner que se ve cuando no está desplegado)
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Vista del elemento en la lista desplegada
        return createView(position, convertView, parent);
    }
}
