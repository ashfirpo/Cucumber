package com.example.cucumber.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.cucumber.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionManager {

    private Context context;
    private SharedPreferences preferences;

    public SessionManager(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
    }


    public void guardarToken(String token)
    {
        SharedPreferences.Editor editor = preferences.edit();

        Date horaToken = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String horaTokenFormateada = formatter.format(horaToken);

        editor.putString("hora_token", horaTokenFormateada);
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken()
    {
        String token = preferences.getString("token", null);
        return token;
    }

    public String getHoraToken()
    {
        String horaToken = preferences.getString("hora_token", null);
        return horaToken;
    }

    public void guardarTokenRefresh(String tokenRefresh)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token_refresh", tokenRefresh);
        editor.commit();
    }

    public String getTokenRefresh()
    {
        String tokenRefresh = preferences.getString("token_refresh", null);
        return tokenRefresh;
    }

    public boolean isTokenVigente() throws ParseException
    {
        String horaToken = getHoraToken();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date horaTokenFormateada = formatter.parse(horaToken);
        Date horaActual = Calendar.getInstance().getTime();

        long millis = horaActual.getTime() - horaTokenFormateada.getTime();
        long minutos = (millis/(1000*60)) % 60;

        if(minutos < 30)
            return true;
        else
            return false;
    }

    public void regenerarToken()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getString(R.string.retrofit_server))
                .build();
        SOAService soaService = retrofit.create(SOAService.class);

        Call<SOAResponse> call = soaService.actualizarToken(getTokenRefresh());
        call.enqueue(new Callback<SOAResponse>()
        {
            @Override
            public void onResponse(Call<SOAResponse> call, Response<SOAResponse> response)
            {
                if (response.isSuccessful())
                {
                    guardarToken(response.body().getToken());
                    guardarTokenRefresh(response.body().getToken_refresh());
                }
                else
                {
                    Toast.makeText(context, String.format("Error en regeneración de token"), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SOAResponse> call, Throwable t)
            {
                Toast.makeText(context,"No se pudo regenerar el token correctamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void guardarEmail(String email)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public String getEmail()
    {
        String email = preferences.getString("email", null);
        return email;
    }
}
