package com.example.dasfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class LoginActivity extends BaseActivity {

    private EditText etNombre, etCorreo, etContrasena;
    private Button btnRegistro, btnLogin, btnIrARegistro;
    private TextView tvPregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNombre      = findViewById(R.id.etNombre);
        etCorreo      = findViewById(R.id.etCorreo);
        etContrasena  = findViewById(R.id.etContrasena);
        btnRegistro   = findViewById(R.id.btnRegistro);
        btnLogin      = findViewById(R.id.btnLogin);
        btnIrARegistro= findViewById(R.id.btnIrARegistro);
        tvPregunta    = findViewById(R.id.tvPregunta);

        etNombre.setVisibility(View.GONE);
        btnRegistro.setVisibility(View.GONE);

        btnRegistro.setOnClickListener(v -> registrarUsuario());
        btnLogin.setOnClickListener(v -> iniciarSesion());

        btnIrARegistro.setOnClickListener(v -> {
            etNombre.setVisibility(View.VISIBLE);
            btnRegistro.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnIrARegistro.setVisibility(View.GONE);
            tvPregunta.setVisibility(View.GONE);
        });
    }

    private void registrarUsuario() {
        String nombre    = etNombre.getText().toString().trim();
        String correo    = etCorreo.getText().toString().trim();
        String contrasena= etContrasena.getText().toString();

        Log.d("DASDEBUG", "Registrando usuario: " + correo);

        new Thread(() -> {
            try {
                URL url = new URL("http://ec2-51-44-167-78.eu-west-3.compute.amazonaws.com/mmartin239/WEB/usuarios/registro.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "nombre=" + URLEncoder.encode(nombre, "UTF-8") +
                        "&correo=" + URLEncoder.encode(correo, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8");

                Log.d("DASDEBUG", "Enviando datos: " + data);

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String result = in.readLine();
                in.close();

                Log.d("DASDEBUG", "Respuesta del servidor: " + result);

                runOnUiThread(() -> {
                    if ("OK".equals(result)) {
                        Toast.makeText(this, getString(R.string.registro_exitoso), Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("correo", correo);
                        editor.putString("nombre_usuario", nombre);
                        editor.putString("foto_url", "");
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.error_registrar, result), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DASDEBUG", "Error en registro", e);
                runOnUiThread(() ->
                        Toast.makeText(this, getString(R.string.error_red), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void iniciarSesion() {
        String correo    = etCorreo.getText().toString().trim();
        String contrasena= etContrasena.getText().toString();

        new Thread(() -> {
            try {
                URL url = new URL("http://ec2-51-44-167-78.eu-west-3.compute.amazonaws.com/mmartin239/WEB/usuarios/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "correo=" + URLEncoder.encode(correo, "UTF-8") +
                        "&contrasena=" + URLEncoder.encode(contrasena, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String result = in.readLine();
                in.close();

                String[] parts = result.split("\\|", -1);

                runOnUiThread(() -> {
                    if (parts.length >= 1 && parts[0].equals("OK")) {
                        String nombre    = (parts.length > 1 ? parts[1] : "");
                        String fotoUrl   = (parts.length > 2 ? parts[2] : "");

                        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("correo", correo);
                        editor.putString("nombre_usuario", nombre);
                        editor.putString("foto_url", fotoUrl);
                        editor.apply();

                        Toast.makeText(this, getString(R.string.sesion_iniciada), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String mensajeError = getString(R.string.usuario_contrasena_incorrectos);
                        if (parts.length >= 1 && parts[0].startsWith("ERROR_")) {
                            mensajeError = parts[0].replace("ERROR_", "");
                        }
                        Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, getString(R.string.error_red), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}