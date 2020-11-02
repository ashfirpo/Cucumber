package com.example.cucumber.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SOAService {

    @POST("register")
    Call<SOAResponse> register(@Body SOARequest request);

    @POST("login")
    Call<SOAResponse> login(@Body SOARequest request);

    @POST("event")
    Call<SOAEventResponse> registrarEvento(@Header("Authorization") String token, @Body SOAEventRequest request);

    @PUT("refresh")
    Call<SOAResponse> actualizarToken(@Header("Authorization") String tokenRefresh);
}
