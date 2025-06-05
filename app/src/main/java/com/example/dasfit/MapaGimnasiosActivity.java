package com.example.dasfit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.dasfit.modelo.Gimnasio;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Marker.OnMarkerClickListener;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapaGimnasiosActivity extends BaseActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    private MapView mapView;
    private List<Gimnasio> listaGimnasios = new ArrayList<>();

    private BottomSheetBehavior<CoordinatorLayout> sheetBehavior;
    private TextView tvNombre;
    private TextView tvDireccion;
    private TextView tvHorario;
    private TextView tvWeb;
    private TextView tvTelefono;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_gimnasios);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        org.osmdroid.api.IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint indautxuPoint = new GeoPoint(43.2640, -2.9350);
        mapController.setCenter(indautxuPoint);

        CoordinatorLayout bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        tvNombre    = findViewById(R.id.tv_gym_nombre);
        tvDireccion = findViewById(R.id.tv_gym_direccion);
        tvHorario   = findViewById(R.id.tv_gym_horario);
        tvWeb       = findViewById(R.id.tv_gym_web);
        tvTelefono  = findViewById(R.id.tv_gym_telefono);
        btnBack = findViewById(R.id.btn_back);

        if (btnBack != null) {
            btnBack.bringToFront();
            btnBack.setOnClickListener(v -> {
                finish();
            });
        }

        poblarListaGimnasios();
        agregarMarcadores();
        pedirPermisos();
    }

    private void poblarListaGimnasios() {
        try {
            InputStream is = getAssets().open("gimnasios.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer, "UTF-8");

            JSONArray arr = new JSONArray(jsonStr);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Gimnasio gym = new Gimnasio(
                        obj.getString("nombre"),
                        obj.getDouble("latitud"),
                        obj.getDouble("longitud"),
                        obj.getString("direccion"),
                        obj.getString("horario"),
                        obj.getString("web"),
                        obj.getString("telefono"),
                        obj.getString("tipo")
                );
                listaGimnasios.add(gym);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_cargar_gimnasios), Toast.LENGTH_LONG).show();
        }
    }

    private void agregarMarcadores() {
        for (Gimnasio gym : listaGimnasios) {
            GeoPoint punto = new GeoPoint(gym.getLatitud(), gym.getLongitud());

            Marker marker = new Marker(mapView);
            marker.setPosition(punto);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            Drawable icono = obtenerIconoPorTipo(gym.getTipo());
            if (icono instanceof BitmapDrawable) {
                marker.setIcon((BitmapDrawable) icono);
            }

            marker.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker clickedMarker, MapView mapView) {
                    // 1. Hacer visible el CardView
                    View card = findViewById(R.id.card_gym_info);
                    card.setVisibility(View.VISIBLE);

                    tvNombre.setText(gym.getNombre());
                    tvDireccion.setText(getString(R.string.gym_info_direccion, gym.getDireccion()));
                    tvHorario.setText(getString(R.string.gym_info_horario, gym.getHorario()));
                    tvWeb.setText(gym.getWeb());
                    tvTelefono.setText(gym.getTelefono());

                    tvNombre.setVisibility(View.VISIBLE);
                    tvDireccion.setVisibility(View.VISIBLE);
                    tvHorario.setVisibility(View.VISIBLE);
                    tvWeb.setVisibility(View.VISIBLE);
                    tvTelefono.setVisibility(View.VISIBLE);

                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return true;
                }
            });

            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private Drawable getTintedMarkerDrawable(int color) {
        Drawable vector = ContextCompat.getDrawable(this, R.drawable.ic_ubi);
        if (vector == null) return null;

        Drawable wrapped = DrawableCompat.wrap(vector).mutate();
        DrawableCompat.setTint(wrapped, color);

        Bitmap bitmap = Bitmap.createBitmap(
                wrapped.getIntrinsicWidth(),
                wrapped.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        wrapped.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        wrapped.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private Drawable obtenerIconoPorTipo(String tipo) {
        switch (tipo) {
            case "FitnessPark":
                return getTintedMarkerDrawable(Color.parseColor("#FFD600")); // Amarillo
            case "BasicFit":
                return getTintedMarkerDrawable(Color.parseColor("#FF9800")); // Naranja
            case "DreamFit":
                return getTintedMarkerDrawable(Color.parseColor("#4CAF50")); // Verde
            case "BeUp":
                return getTintedMarkerDrawable(Color.parseColor("#03A9F4")); // Azul
            case "VivaGym":
                return getTintedMarkerDrawable(Color.parseColor("#F44336")); // Rojo
            case "Altafit":
                return getTintedMarkerDrawable(Color.parseColor("#9C27B0")); // Morado
            case "Synergym":
                return getTintedMarkerDrawable(Color.GRAY);                // Gris
            case "BETiFIT":
                return getTintedMarkerDrawable(Color.BLACK);               // Negro
            case "Summit Fitness Club":
                return getTintedMarkerDrawable(Color.parseColor("#795548")); // Marrón
            default:
                return getTintedMarkerDrawable(Color.DKGRAY);               // Gris oscuro
        }
    }

    private void pedirPermisos() {
        String[] permisos = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        List<String> permisosNoConcedidos = new ArrayList<>();
        for (String perm : permisos) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                permisosNoConcedidos.add(perm);
            }
        }
        if (!permisosNoConcedidos.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permisosNoConcedidos.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            );
        } else {
            habilitarMiUbicacion();
        }
    }

    private void habilitarMiUbicacion() {
        MyLocationNewOverlay locOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(this),
                mapView
        );
        locOverlay.enableMyLocation();
        mapView.getOverlays().add(locOverlay);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            boolean todosConcedidos = true;
            for (int res : grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    todosConcedidos = false;
                    break;
                }
            }
            if (todosConcedidos) {
                habilitarMiUbicacion();
            } else {
                Toast.makeText(
                        this,
                        getString(R.string.permisos_localizacion_denegados),
                        Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}