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

public class MapaGimnasiosActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    private MapView mapView;
    private List<Gimnasio> listaGimnasios = new ArrayList<>();

    // Campos para el BottomSheet
    private BottomSheetBehavior<CoordinatorLayout> sheetBehavior;
    private TextView tvNombre;
    private TextView tvDireccion;
    private TextView tvHorario;
    private TextView tvWeb;
    private TextView tvTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Cargar configuración de osmdroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // 2. Asignar layout
        setContentView(R.layout.activity_mapa_gimnasios);

        // 3. Vincular MapView
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        // Ya no mostramos CustomZoomButtonsController explícitamente
        mapView.setMultiTouchControls(true);

        // 4. Centrar en Indautxu/San Mamés con zoom más cercano
        org.osmdroid.api.IMapController mapController = mapView.getController();
        mapController.setZoom(15.0); // Zoom 15 para zona urbana
        GeoPoint indautxuPoint = new GeoPoint(43.2640, -2.9350);
        mapController.setCenter(indautxuPoint);

        // 5. Vincular views del BottomSheet
        CoordinatorLayout bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        tvNombre    = findViewById(R.id.tv_gym_nombre);
        tvDireccion = findViewById(R.id.tv_gym_direccion);
        tvHorario   = findViewById(R.id.tv_gym_horario);
        tvWeb       = findViewById(R.id.tv_gym_web);
        tvTelefono  = findViewById(R.id.tv_gym_telefono);

        // 6. Cargar datos de gimnasios desde JSON
        poblarListaGimnasios();

        // 7. Añadir marcadores
        agregarMarcadores();

        // 8. Solicitar permiso de localización si aún no está concedido
        pedirPermisos();
    }

    /**
     * Lee el JSON de assets/gimnasios.json y pobla la lista.
     */
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
            Toast.makeText(this, "Error cargando lista de gimnasios", Toast.LENGTH_LONG).show();
        }
    }

    private void agregarMarcadores() {
        for (Gimnasio gym : listaGimnasios) {
            GeoPoint punto = new GeoPoint(gym.getLatitud(), gym.getLongitud());

            Marker marker = new Marker(mapView);
            marker.setPosition(punto);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            // Icono: vector tintado según tipo
            Drawable icono = obtenerIconoPorTipo(gym.getTipo());
            if (icono instanceof BitmapDrawable) {
                marker.setIcon((BitmapDrawable) icono);
            }

            // Al hacer clic, mostrar BottomSheet con los datos
            marker.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker clickedMarker, MapView mapView) {
                    // 1. Hacer visible el CardView
                    View card = findViewById(R.id.card_gym_info);
                    card.setVisibility(View.VISIBLE);

                    // 2. Rellenar los TextView
                    tvNombre.setText(gym.getNombre());
                    tvDireccion.setText("Dirección: " + gym.getDireccion());
                    tvHorario.setText("Horario: " + gym.getHorario());
                    tvWeb.setText(gym.getWeb());
                    tvTelefono.setText(gym.getTelefono());

                    // 3. Marcar visible cada TextView (en el XML estaban “gone”)
                    tvNombre.setVisibility(View.VISIBLE);
                    tvDireccion.setVisibility(View.VISIBLE);
                    tvHorario.setVisibility(View.VISIBLE);
                    tvWeb.setVisibility(View.VISIBLE);
                    tvTelefono.setVisibility(View.VISIBLE);

                    // 4. Expandir BottomSheet
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return true;
                }
            });

            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    /**
     * Convierte el vector ic_ubi.xml en un BitmapDrawable tintado del color indicado.
     */
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

    /**
     * Selecciona un color de tintado para ic_ubi.xml según el tipo de gimnasio.
     */
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
                Toast.makeText(this,
                        "Permisos de localización denegados. La ubicación no se mostrará.",
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