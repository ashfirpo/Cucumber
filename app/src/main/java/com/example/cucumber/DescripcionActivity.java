package com.example.cucumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.example.cucumber.API.SOAEventRequest;
import com.example.cucumber.API.SOAEventResponse;
import com.example.cucumber.API.SOAService;
import com.example.cucumber.API.SessionManager;
import com.example.cucumber.Firebase.Planta;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescripcionActivity extends AppCompatActivity {

    private Planta planta;
    private ArrayList<Planta> plantasFav;

    private TextView txtDescripcion, txtRiego, txtMaceta, txtExtras;
    private TextView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12;
    private TextView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12;

    private AppBarLayout appbar;
    private FloatingActionButton fab;


    private SensorManager sensorManager;
    private DetectorShake detectorShake;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);



        sessionManager = new SessionManager(getApplicationContext());

        configurarElementos();
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        configurarShake();
        inicializarSensores();

        planta = (Planta)getIntent().getSerializableExtra("PlantaSeleccionada");
        plantasFav = (ArrayList<Planta>)getIntent().getSerializableExtra("plantasFav");

        Log.i("Cucumber", "onCreate");

        if (planta == null) {
            Toast.makeText(DescripcionActivity.this, "No se encontró la planta seleccionada", Toast.LENGTH_LONG);
        } else
            cargarDatosPlanta();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarSensores();
    }

    @Override
    protected void onPause() {
        pararSensores();
        super.onPause();
    }

    @Override
    protected void onStop() {
        pararSensores();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        inicializarSensores();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        pararSensores();
        super.onDestroy();
    }


    protected void inicializarSensores() {
        sensorManager.registerListener(detectorShake, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    private void pararSensores() {
        sensorManager.unregisterListener(detectorShake);
    }

    private DetectorShake.OnShakeListener shakeListener = new DetectorShake.OnShakeListener() {
        @Override
        public void onShake(int count) {
            //Guardamos el resultado de la fuerza G y registramos el evento
            guardarPreferenciasAcelerometro(count);
            registrarEventoAcelerometro();
        }
    };


    private void configurarElementos() {
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtExtras = findViewById(R.id.txtExtras);
        txtMaceta = findViewById(R.id.lblMaceta);
        txtRiego = findViewById(R.id.lblFrecuenciaRiego);

        appbar = findViewById(R.id.app_bar);

        s1 = findViewById(R.id.txtS1);
        s2 = findViewById(R.id.txtS2);
        s3 = findViewById(R.id.txtS3);
        s4 = findViewById(R.id.txtS4);
        s5 = findViewById(R.id.txtS5);
        s6 = findViewById(R.id.txtS6);
        s7 = findViewById(R.id.txtS7);
        s8 = findViewById(R.id.txtS8);
        s9 = findViewById(R.id.txtS9);
        s10 = findViewById(R.id.txtS10);
        s11 = findViewById(R.id.txtS11);
        s12 = findViewById(R.id.txtS12);

        c1 = findViewById(R.id.txtC1);
        c2 = findViewById(R.id.txtC2);
        c3 = findViewById(R.id.txtC3);
        c4 = findViewById(R.id.txtC4);
        c5 = findViewById(R.id.txtC5);
        c6 = findViewById(R.id.txtC6);
        c7 = findViewById(R.id.txtC7);
        c8 = findViewById(R.id.txtC8);
        c9 = findViewById(R.id.txtC9);
        c10 = findViewById(R.id.txtC10);
        c11 = findViewById(R.id.txtC11);
        c12 = findViewById(R.id.txtC12);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
    }

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    private void configurarShake() {
        Intent i = new Intent(this, ServicioShake.class);
        detectorShake = new DetectorShake();
        detectorShake.setOnShakeListener(shakeListener);
    }

    public void guardarPreferenciasAcelerometro(float g) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("Acelerometro", g);
        editor.apply();
    }

    public void registrarEventoAcelerometro()
    {
        SOAEventRequest request = new SOAEventRequest();
        request.setEnv("TEST");
        request.setType_events("Acelerometro");
        request.setDescription("Shake");

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.retrofit_server)).build();

        SOAService service = retrofit.create(SOAService.class);

        try
        {
            if(!sessionManager.isTokenVigente())
                sessionManager.regenerarToken();
        }
        catch(Exception e)
        {
            sessionManager.regenerarToken();
        }

        Call<SOAEventResponse> call = service.registrarEvento("Bearer " + sessionManager.getToken(), request);
        call.enqueue(new Callback<SOAEventResponse>() {
            @Override
            public void onResponse(Call<SOAEventResponse> call, Response<SOAEventResponse> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Se registró el evento Shake", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error en el registro Shake", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SOAEventResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló el registro de Shake", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void cargarDatosPlanta()
    {
        String extras = "\n \n";


        try{getSupportActionBar().setTitle(planta.getNombre());}catch (Exception e){}

        txtDescripcion.setText(planta.getDescripcion());
        txtMaceta.setText(planta.getMaceta());
        txtRiego.setText(planta.getRiego());


       for(String e : planta.getExtra())
        {
            extras = extras + e + " \n";
           // extras.concat(e.toString() + "\n");
        }

        txtExtras.setText(extras);

        if(plantasFav.contains(planta))
        {
            //la planta ya es fav, darle la opcion de desfav
        }

        //Log.i("Cucumber", "Cargar datos de planta: " + planta.getNombre());
    }
}
