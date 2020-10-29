package com.example.cucumber.servicios;

import com.example.cucumber.API.SOARequest;
import com.example.cucumber.API.SOAResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SOAService {

    @POST("api/register")
    Call<SOAResponse> register(@Body SOARequest request);


}
