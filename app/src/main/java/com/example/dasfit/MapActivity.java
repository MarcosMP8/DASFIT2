package com.example.dasfit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.preference.PreferenceManager;
import com.google.android.gms.location.*;

public class MapActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private static final long UPDATE_INTERVAL_MS = 10_000;     // 10s
    private static final long FASTEST_INTERVAL_MS = 5_000;     // 5s

    private MapView mapView;
    private FusedLocationProviderClient fusedClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker userMarker;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance()
                .load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);


        fusedClient = LocationServices.getFusedLocationProviderClient(this);


        locationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                for (Location loc : result.getLocations()) {
                    lastLocation = loc;
                    updateMapLocation(loc);
                }
            }
        };

        Button btnCenter = findViewById(R.id.btnCenterLocation);
        btnCenter.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                fusedClient
                        .getCurrentLocation(
                                LocationRequest.PRIORITY_HIGH_ACCURACY,
                                /* cancellationToken= */ null
                        )
                        .addOnSuccessListener(loc -> {
                            if (loc != null) {
                                updateMapLocation(loc);
                            }
                        });
            }
        });


        if (hasLocationPermission()) {
            startLocationUpdates();
        } else {
            requestLocationPermission();
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void startLocationUpdates() {fusedClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }

    private void updateMapLocation(Location loc) {
        GeoPoint pt = new GeoPoint(loc.getLatitude(), loc.getLongitude());

        if (userMarker == null) {
            userMarker = new Marker(mapView);
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            userMarker.setIcon(getResources().getDrawable(
                    org.osmdroid.library.R.drawable.marker_default, null));
            mapView.getOverlays().add(userMarker);
        }

        userMarker.setPosition(pt);
        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(pt);
        mapView.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] perms,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
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
        fusedClient.removeLocationUpdates(locationCallback);
        mapView.onPause();
    }
}