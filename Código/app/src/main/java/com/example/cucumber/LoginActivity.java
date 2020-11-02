package com.example.cucumber;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cucumber.API.SOAEventRequest;
import com.example.cucumber.API.SOAEventResponse;
import com.example.cucumber.API.SOARequest;
import com.example.cucumber.API.SOAResponse;
import com.example.cucumber.API.SOAService;
import com.example.cucumber.API.SessionManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private EditText username, password;
    private Button btnLogin, btnRegistroLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Innicializamos el manejador de sesión
        sessionManager = new SessionManager(getApplicationContext());
        configurarElementos();
    }


    private void configurarElementos()
    {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnRegistroLogin = findViewById(R.id.btnRegistrarseLogin);

        btnLogin.setOnClickListener(botonListener);
        btnRegistroLogin.setOnClickListener(botonListener);
        username.addTextChangedListener(tw);
        password.addTextChangedListener(tw);
    }

    //Para verificar que los campos sean correctos
    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            verificarCampos();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    View.OnClickListener botonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Si hay conexión, crea la request
            switch (v.getId())
            {
                case R.id.login:
                    if(hayConexion())
                        crearRequest();
                    else
                        Snackbar.make(v, "No hay conexión a Internet", BaseTransientBottomBar.LENGTH_INDEFINITE);
                    break;

                case R.id.btnRegistrarseLogin:
                    startActivity(new Intent(LoginActivity.this, RegistrarseActivity.class));
                    finish();
                    break;
            }
        }
    };


    private void crearRequest()
    {
        //Creamos la request
        SOARequest request = new SOARequest();
        request.setEnv(getString(R.string.API_environment));
        request.setEmail(username.getText().toString());
        request.setPassword(password.getText().toString());

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.retrofit_server)).build();

        SOAService service = retrofit.create(SOAService.class);

        Call<SOAResponse> call = service.login(request);
        call.enqueue(new Callback<SOAResponse>() {
            @Override
            public void onResponse(Call<SOAResponse> call, Response<SOAResponse> response) {
                if(response.isSuccessful())
                {
                    //Obtenemos los datos de sesión que nos da el servidor
                    sessionManager.guardarEmail(request.getEmail());
                    sessionManager.guardarToken(response.body().getToken());
                    sessionManager.guardarTokenRefresh(response.body().getToken_refresh());

                    //Registramos el evento del login
                    registrarEventoLogin();

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No se encontró al usuario", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SOAResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló el Login", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean hayConexion()
    {
        //Verificamos la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    private void registrarEventoLogin()
    {
        SOAEventRequest request = new SOAEventRequest();
        request.setEnv(getString(R.string.API_environment));
        request.setType_events("Login");
        request.setDescription("Se realizó un Login");

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.retrofit_server)).build();

        SOAService service = retrofit.create(SOAService.class);

        Call<SOAEventResponse> call = service.registrarEvento("Bearer " + sessionManager.getToken(), request);
        call.enqueue(new Callback<SOAEventResponse>() {
            @Override
            public void onResponse(Call<SOAEventResponse> call, Response<SOAEventResponse> response) {
                if(!response.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Error al registrar el Login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SOAEventResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló el registro del Login", Toast.LENGTH_LONG);
            }
        });
    }


    private boolean verificarCampos()
    {
        if(validarUsuario(username.getText().toString()) && validarPass(password.getText().toString()))
        {
            btnLogin.setEnabled(true);
            return true;
        }
        return false;
    }


    private boolean validarUsuario(String user)
    {
        return !user.isEmpty();
    }

    private boolean validarPass(String pass)
    {
        return (!pass.isEmpty() && pass.length() >= 8);
    }
}
