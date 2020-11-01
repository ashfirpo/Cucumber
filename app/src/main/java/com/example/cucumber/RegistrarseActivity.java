package com.example.cucumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cucumber.API.SOARequest;
import com.example.cucumber.API.SOAResponse;
import com.example.cucumber.API.SOAService;
import com.example.cucumber.API.SessionManager;
import com.example.cucumber.Firebase.Firebase;
import com.example.cucumber.Firebase.Usuario;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarseActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private DatabaseReference reference;
    private Usuario user;

    private EditText txtNombre, txtApellido, txtDNI, txtEmail, txtPass, txtComision;
    private Button btnRegistrarse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        sessionManager = new SessionManager(getApplicationContext());

        reference = Firebase.getFirebaseDatabase().getReference("/users/");

        configurarElementos();
    }


    private void configurarElementos() {
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtDNI = findViewById(R.id.txtDNI);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPassword);
        txtComision = findViewById(R.id.txtComision);
        btnRegistrarse = findViewById(R.id.btnRegistrar);

        btnRegistrarse.setOnClickListener(botonListener);
        txtApellido.addTextChangedListener(tw);
        txtComision.addTextChangedListener(tw);
        txtDNI.addTextChangedListener(tw);
        txtEmail.addTextChangedListener(tw);
        txtNombre.addTextChangedListener(tw);
        txtPass.addTextChangedListener(tw);
    }

    View.OnClickListener botonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(hayConexion())
                crearRequest();
            else
                Snackbar.make(v, "No hay conexión a Internet", BaseTransientBottomBar.LENGTH_INDEFINITE);
        }
    };

    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private boolean hayConexion()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }



    private boolean verificarCampos()
    {
        if(txtNombre.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Falta ingresar Nombre", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtApellido.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Falta ingresar Apellido", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtDNI.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Falta ingresar DNI", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtEmail.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Falta ingresar Email", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtPass.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Falta ingresar Contraseña", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtPass.getText().toString().length() < 8)
        {
            Toast.makeText(getApplicationContext(), "La contraseña debe ser mayor a 8 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if(txtComision.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Falta ingresar Comisión", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void crearRequest()
    {
        SOARequest request = new SOARequest();
        request.setEnv("TEST");
        request.setName(txtNombre.getText().toString());
        request.setLastname(txtApellido.getText().toString());
        request.setDni(Long.parseLong(txtDNI.getText().toString()));
        request.setEmail(txtEmail.getText().toString());
        request.setPassword(txtPass.getText().toString());
        request.setCommission(Long.parseLong(txtComision.getText().toString()));



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.retrofit_server)).build();

        SOAService service = retrofit.create(SOAService.class);

        Call<SOAResponse> call = service.register(request);
        call.enqueue(new Callback<SOAResponse>() {
            @Override
            public void onResponse(Call<SOAResponse> call, Response<SOAResponse> response) {
                if(response.isSuccessful())
                {
                    user = new Usuario();
                    user.setEmail(request.getEmail());
                    agregarUsuario(user);

                    sessionManager.guardarEmail(request.getEmail());
                    sessionManager.guardarToken(response.body().getToken());
                    sessionManager.guardarTokenRefresh(response.body().getToken_refresh());

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error en el Registro", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SOAResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló la Registración", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void agregarUsuario(Usuario user)
    {
        reference.push().setValue(user);
    }
}
