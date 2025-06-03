package com.example.dasfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.dasfit.network.ApiService;
import com.example.dasfit.network.GenericResponse;
import com.example.dasfit.network.RetrofitClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PerfilActivity extends BaseActivity {
    private TextView tvNombrePerfil, tvCorreoPerfil;
    private EditText etNombrePerfil, etCorreoPerfil;
    private LinearLayout layoutVista, layoutEditar;
    private SharedPreferences prefs;
    private Button btnGuardarPerfil, btnCerrarSesion;
    private ImageButton btnEditar, btnAjustes;
    private ImageView ivPerfil;
    private static final String BASE_URL =
            "http://ec2-51-44-167-78.eu-west-3.compute.amazonaws.com/mmartin239/WEB/usuarios/";
    private ApiService api;
    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bitmap thumb = result.getData().getExtras().getParcelable("data");
                            ivPerfil.setImageBitmap(thumb);
                            uploadImageToServer(thumb);
                        }
                    }
            );

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        if (granted) openCamera();
                        else Toast.makeText(this, getString(R.string.permiso_camara_denegado), Toast.LENGTH_SHORT).show();
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String nombre = prefs.getString("nombre_usuario", "");
        String correo = prefs.getString("correo", "");

        tvNombrePerfil = findViewById(R.id.tvNombrePerfil);
        tvCorreoPerfil = findViewById(R.id.tvCorreoPerfil);
        etNombrePerfil = findViewById(R.id.etNombrePerfil);
        etCorreoPerfil = findViewById(R.id.etCorreoPerfil);
        layoutVista = findViewById(R.id.layoutVista);
        layoutEditar = findViewById(R.id.layoutEditar);
        btnEditar = findViewById(R.id.btnEditarNombre);
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        ivPerfil = findViewById(R.id.ivPerfil);
        btnAjustes = findViewById(R.id.btnAjustes);

        String fotoUrlPref = prefs.getString("foto_url", "");
        if (!fotoUrlPref.isEmpty()) {
            Glide.with(this)
                    .load(fotoUrlPref)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(ivPerfil);
        } else {
            String nombreFoto = "foto_" + correo.replaceAll("[^a-zA-Z0-9]", "") + ".jpg";
            String urlDefault = BASE_URL + "fotos_perfil/" + nombreFoto;
            Glide.with(this)
                    .load(urlDefault)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(ivPerfil);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        btnAjustes.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.ajustes), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PerfilActivity.this, AjustesActivity.class);
            startActivity(intent);
        });

        tvNombrePerfil.setText(nombre);
        tvCorreoPerfil.setText(correo);

        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnEditar.setOnClickListener(v -> {
            etNombrePerfil.setText(tvNombrePerfil.getText().toString());
            etCorreoPerfil.setText(tvCorreoPerfil.getText().toString());
            layoutVista.setVisibility(View.GONE);
            layoutEditar.setVisibility(View.VISIBLE);
        });

        btnGuardarPerfil.setOnClickListener(v -> {
            String correoOriginal = prefs.getString("correo", "");
            actualizarPerfil(correoOriginal);
        });

        ivPerfil.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        api = RetrofitClient
                .getInstance()
                .create(ApiService.class);
    }

        private void actualizarPerfil(String correoOriginal) {
            String nuevoNombre = etNombrePerfil.getText().toString();
            String nuevoCorreo = etCorreoPerfil.getText().toString();

            new Thread(() -> {
                try {
                    URL url = new URL("http://ec2-51-44-167-78.eu-west-3.compute.amazonaws.com/mmartin239/WEB/usuarios/update_perfil.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    String postData = "correo_actual=" + URLEncoder.encode(correoOriginal, "UTF-8")
                            + "&nombre_nuevo=" + URLEncoder.encode(nuevoNombre, "UTF-8")
                            + "&correo_nuevo=" + URLEncoder.encode(nuevoCorreo, "UTF-8");

                    Log.d("DASDEBUG", "Enviando datos: " + postData);

                    OutputStream os = conn.getOutputStream();
                    os.write(postData.getBytes());
                    os.flush();
                    os.close();

                    InputStream inputStream = (conn.getResponseCode() >= 400)
                            ? conn.getErrorStream()
                            : conn.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    String result = sb.toString().trim();
                    Log.d("DASDEBUG", "Respuesta del servidor: " + result);
                    reader.close();

                    runOnUiThread(() -> {
                        if (result.equals("OK")) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("correo", nuevoCorreo);
                            editor.putString("nombre_usuario", nuevoNombre);
                            editor.apply();

                            tvNombrePerfil.setText(nuevoNombre);
                            tvCorreoPerfil.setText(nuevoCorreo);
                            layoutEditar.setVisibility(View.GONE);
                            layoutVista.setVisibility(View.VISIBLE);

                            Toast.makeText(this, getString(R.string.perfil_actualizado), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.error_actualizar_perfil), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.e("DASDEBUG", "Error de conexión", e);
                    runOnUiThread(() -> Toast.makeText(this, getString(R.string.error_conexion), Toast.LENGTH_SHORT).show());
                }
            }).start();
        }

    private void openCamera() {
        takePictureLauncher.launch(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        );
    }

    private void uploadImageToServer(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        String b64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        String correo = prefs.getString("correo", "");
        api.uploadProfileImage(correo, b64)
                .enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call,
                                           Response<GenericResponse> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isSuccess()) {
                            String url = response.body().getUrl();
                            Log.d("PROFILE", "URL guardada = " + url);
                            prefs.edit().putString("foto_url", url).apply();
                            Glide.with(PerfilActivity.this)
                                    .load(url)
                                    .placeholder(R.drawable.ic_user)
                                    .error(R.drawable.ic_user)
                                    .into(ivPerfil);
                            Toast.makeText(PerfilActivity.this,
                                    getString(R.string.foto_actualizada),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(PerfilActivity.this,
                                getString(R.string.fallo_red, t.getMessage()),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}