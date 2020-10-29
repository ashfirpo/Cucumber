package com.example.cucumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cucumber.API.SOARequest;
import com.example.cucumber.API.SOAResponse;
import com.example.cucumber.servicios.SOAService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtDNI;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtComision;

    private Button btnRegistrarse;

    private TextView lblToken;
    private TextView lblTokenRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

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

        lblToken = findViewById(R.id.lblToken);
        lblTokenRefresh = findViewById(R.id.lblTokenRefresh);

        setearListeners();
    }

    private void setearListeners()
    {
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Instanciamos el objeto SOARequest

                SOARequest request = new SOARequest();
                request.setEnv("TEST");
                request.setName(txtNombre.getText().toString());
                request.setLastname(txtApellido.getText().toString());
                request.setDni(Long.parseLong(txtDNI.getText().toString()));
                request.setEmail(txtEmail.getText().toString());
                request.setPassword(txtPass.getText().toString());
                request.setCommission(Long.parseLong(txtComision.getText().toString()));

                //Configuracion de Retrofit

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(getString(R.string.retrofit_server)).build();

                //Creamos el servicio en donde le pasamos la interface que creamos
                SOAService soaService = retrofit.create(SOAService.class);

                //Para empezar a generar una lista de llamados/peticiones, trabajamos con enqueue
                //Retrofit encola cada una de las peticiones y las va encolando de manera asincronica
                //sincronizandose con el hilo principal
                //Se encola un callback, que va a ejecutar la peticion, y cuando tenga la respuesta del servidor
                //va a salirpor "onResponse" o por "onFailed". Si hay respuesta, sea cual sea, cae en OnResponse
                //Despues vemos dentro de onResponse si falló o no. onFailed es mas para algun error en las configuraciones a nivel
                //software, si no se mapea bien el objeto o esta mal configurado el servicio, cosas como esas.


                Call<SOAResponse> call = soaService.register(request);
                call.enqueue((new Callback<SOAResponse>() {
                    @Override
                    public void onResponse(Call<SOAResponse> call, Response<SOAResponse> response) {

                        if(response.isSuccessful())
                        {
                            //todo bien
                            Log.i("Cucumber | Retrofit", "Response exitoso en ambiente: " + response.body().getEnv());
                            Toast.makeText(RegistrarseActivity.this, "Retrofit OK!", Toast.LENGTH_LONG);
                            lblToken.setText(response.body().getToken());
                            lblTokenRefresh.setText(response.body().getToken_refresh());
                        }
                        else
                        {
                            Log.i("Cucumber | Retrofit", "Response fallido en ambiente: " + response.body().getEnv());
                            Toast.makeText(RegistrarseActivity.this, "Retrofit FAILED!", Toast.LENGTH_LONG);
                            lblToken.setText("Respuesta del servidor:");
                            lblTokenRefresh.setText(response.errorBody().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<SOAResponse> call, Throwable t) {

                    }
                }));
            }
        });
    }
}
