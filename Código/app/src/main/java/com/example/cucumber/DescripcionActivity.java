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
import com.example.cucumber.Firebase.Firebase;
import com.example.cucumber.Firebase.Planta;
import com.example.cucumber.Firebase.Usuario;
import com.example.cucumber.Shake.DetectorShake;
import com.example.cucumber.Shake.ServicioShake;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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
    private boolean isFav;
    private ArrayList<Planta> plantasFav;

    private TextView txtDescripcion, txtRiego, txtMaceta, txtExtras;
    private TextView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12;
    private TextView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12;

    private CollapsingToolbarLayout appbar;
    private ImageView image_appbar;
    private FloatingActionButton fab;

    private SensorManager sensorManager;
    private DetectorShake detectorShake;
    private SessionManager sessionManager;

    private DatabaseReference ref;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        //Inicializamos el manejador de sesión
        sessionManager = new SessionManager(getApplicationContext());

        //Inicializamos los datos de las plantas recibidos
        planta = (Planta)getIntent().getSerializableExtra("PlantaSeleccionada");
        plantasFav = (ArrayList<Planta>)getIntent().getSerializableExtra("plantasFav");
        user = (Usuario)getIntent().getSerializableExtra("user");

        //Inicializamos la referencia a la base de datos
        ref = Firebase.getFirebaseDatabase().getReference("/users");
        ref.keepSynced(true);

        configurarElementos();

        if (planta == null) {
            Toast.makeText(DescripcionActivity.this, "No se encontró la planta seleccionada", Toast.LENGTH_LONG).show();
        } else {
            verificarFavorita();
            cargarDatosPlanta();
        }

        //Inicializamos al sensorManager y a los sensores para el Shake
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        configurarShake();
        inicializarSensores();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarSensores();
        cargarDatosPlanta();
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
        //Nos registramos a la detección del shake
        sensorManager.registerListener(detectorShake, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    private void pararSensores() {
        //Se da de baja el shake
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

        appbar = findViewById(R.id.toolbar_layout);
        image_appbar = findViewById(R.id.image_appbar);
        setSupportActionBar(findViewById(R.id.toolbarDesc));

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
            //Agrega y/o elimina de favoritos a la planta
            Snackbar.make(v, actualizarPlantasFavoritas(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };

    private void configurarShake() {
        //Iniciamos el servicio para el Shake
        Intent i = new Intent(this, ServicioShake.class);
        startService(i);
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
        //Creamos la request
        SOAEventRequest request = new SOAEventRequest();
        request.setEnv(getString(R.string.API_environment));
        request.setType_events("Acelerometro");
        request.setDescription("Shake");

        //Iniciamos Retrofit con la URL de la API
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getString(R.string.retrofit_server)).build();

        SOAService service = retrofit.create(SOAService.class);

        //Vemos si hace falta regenerar el token del usuario
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

        //Cargamos la imagen de la planta y sus datos
        try
        {
            Picasso.get().load(planta.getImagen()).into(image_appbar);
            appbar.setTitle(planta.getNombre());
        }catch (Exception e){}

        txtDescripcion.setText(planta.getDescripcion());
        txtMaceta.setText(planta.getMaceta());
        txtRiego.setText(planta.getRiego());

       for(String e : planta.getExtra())
        {
            extras = extras + e + " \n \n";
        }
        txtExtras.setText(extras);

       //Indicamos los meses de plantado y cosecha
        setearPlantado(planta.getPlantar());
        setearCosecha(planta.getCosechar());
    }

    private void setearPlantado(ArrayList<String> plantar)
    {
        for(String s : plantar)
        {
            switch(s)
            {
                case "1":
                    s1.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "2":
                    s2.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "3":
                    s3.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "4":
                    s4.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "5":
                    s5.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "6":
                    s6.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "7":
                    s7.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "8":
                    s8.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "9":
                    s9.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "10":
                    s10.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "11":
                s11.setBackgroundResource(R.color.colorPlantar);
                    break;
                case "12":
                    s12.setBackgroundResource(R.color.colorPlantar);
                    break;

                default:
                    break;
            }
        }
    }

    private void setearCosecha(ArrayList<String> cosechar)
    {
        for(String s : cosechar)
        {
            switch(s)
            {
                case "1":
                    c1.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "2":
                    c2.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "3":
                    c3.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "4":
                    c4.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "5":
                    c5.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "6":
                    c6.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "7":
                    c7.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "8":
                    c8.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "9":
                    c9.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "10":
                    c10.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "11":
                    c11.setBackgroundResource(R.color.colorCosechar);
                    break;
                case "12":
                    c12.setBackgroundResource(R.color.colorCosechar);
                    break;

                default:
                    break;
            }
        }
    }

    private String actualizarPlantasFavoritas()
    {
        String mensaje ="";

        if(isFav)
        {
            //Si ya es favorita, la está eliminando
            plantasFav.remove(planta);
            fab.setImageResource(R.drawable.ic_unfavorite_24dp);
            isFav = false;
            mensaje = "Se eliminó " + planta.getNombre() + " de Favoritos";
        }
        else
        {
            //Si no es favorita, la agrega
            plantasFav.add(planta);
            fab.setImageResource(R.drawable.ic_favorite_24dp);
            isFav = true;
            mensaje = "Se agregó " + planta.getNombre() + " a Favoritos";
        }
        actualizarBDPlantasFavoritas();
        return mensaje;
    }

    private void actualizarBDPlantasFavoritas()
    {
        //Actualiza en la base de datos las plantas favoritas del usuario
        ArrayList<String> favs = new ArrayList<>();
        for(Planta p : plantasFav) {
            favs.add(p.getIdPlanta());
        }

        ref.child(user.getId()).child("plantasFav").setValue(favs);
    }

    private void verificarFavorita()
    {
        //Verifica si la planta ya es una planta favorita
        if(plantasFav.contains(planta))
        {
            isFav = true;
            fab.setImageResource(R.drawable.ic_favorite_24dp);
        }
        else
        {
            isFav = false;
            fab.setImageResource(R.drawable.ic_unfavorite_24dp);
        }
    }
}
