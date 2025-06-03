package com.example.dasfit.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    // Subida de foto de perfil
    @FormUrlEncoded
    @POST("upload_foto.php")
    Call<GenericResponse> uploadProfileImage(
            @Field("correo") String correo,
            @Field("foto")   String fotoBase64
    );
}