package com.example.cucumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.cucumber.Firebase.Firebase;
import com.example.cucumber.Firebase.Planta;
import com.example.cucumber.Firebase.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class ListadoPlantasActivity extends AppCompatActivity implements SensorEventListener {

    private GridView listado;
    private PlantasAdapter adapter;
    private ArrayList<Planta> plantas, plantasFav;
    private SensorManager sensorManager;
    //private SessionManager sessionManager;
    private DatabaseReference refUserPlantas;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantas);

        //Inicializamos el manejador de sesion
       // sessionManager = new SessionManager(getApplicationContext());

        //Inicializamos el listado de plantas
        plantas = new ArrayList<Planta>();
        plantas = (ArrayList<Planta>)getIntent().getSerializableExtra("listadoPlantas");
        //plantasFav = (ArrayList<Planta>)getIntent().getSerializableExtra("plantasFav");
        user = (Usuario)getIntent().getSerializableExtra("user");

        //Instanciamos las referencias a la base de datos
        refUserPlantas = Firebase.getFirebaseDatabase().getReference("/users");
        refUserPlantas.keepSynced(true);
        refUserPlantas.addValueEventListener(userListener);

        //Inicializamos el sensorManager y los sensores
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        inicializarSensores();

        configurarElementos();
        cargarVista();
    }

    @Override
    protected void onStart() {
        inicializarSensores();
        super.onStart();
    }

    @Override
    protected void onPause() {
        pararSensores();
        super.onPause();
    }

    @Override
    protected void onResume() {
        inicializarSensores();
        super.onResume();
    }

    @Override
    protected void onStop() {
        pararSensores();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        pararSensores();
        super.onDestroy();
    }


    private void inicializarSensores()
    {
        //Se registra el sensorManager a los sensores correspondientes
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void pararSensores()
    {
        //Se cancela la suscripción a los sensores
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Sincronizamos los sensores, tomamos el valor y lo guardamos
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    guardarPreferenciasAcelerometro(event.values[0]);
                    break;

                case Sensor.TYPE_PROXIMITY:
                    guardarPreferenciasProximidad(event.values[0]);
                    break;

                case Sensor.TYPE_LIGHT:
                    guardarPreferenciasLuz(event.values[0]);
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {}


    private void configurarElementos()
    {
        listado = findViewById(R.id.gridPlantas);
    }

    private void cargarVista()
    {
        //Seteamos el adapter para las plantas a la grilla
        adapter = new PlantasAdapter(ListadoPlantasActivity.this, plantas);
        listado.setAdapter(adapter);

        listado.setOnItemClickListener(listadoListener);
    }

    private AdapterView.OnItemClickListener listadoListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Cuando se seleccione una planta, nos muestra el detalle de la misma
            Intent i = new Intent(getApplicationContext(), DescripcionActivity.class);
            i.putExtra("PlantaSeleccionada",((Planta)listado.getItemAtPosition(position)));
            i.putExtra("plantasFav", Planta.obtenerObjetosPlanta(plantas, user.getPlantasFav()));
            i.putExtra("user", user);

            startActivity(i);
        }
    };

    private ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            //Si hubo cambios en las plantas favoritas del usuario, actualizamos esos valores
            Usuario u = snapshot.child(user.getId()).getValue(Usuario.class);
            if(u.getPlantasFav() != null)
            {
                user.setPlantasFav(u.getPlantasFav());
                plantasFav = Planta.obtenerObjetosPlanta(plantas, user.getPlantasFav());
                return;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), "Error en la actualización de datos", Toast.LENGTH_SHORT).show();
        }
    };


    private void guardarPreferenciasAcelerometro(float g)
    {
        //Guardamos en SharedPreferences los datos del acelerómetro
        SharedPreferences sp = getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Acelerometro", g);
        editor.apply();
    }

    private void guardarPreferenciasProximidad(float cm)
    {
        //Guardamos en SharedPreferences los datos de la proximidad
        SharedPreferences sp = getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Proximidad", cm);
        editor.apply();
    }

    private void guardarPreferenciasLuz(float luz)
    {
        //Guardamos en SharedPreferences los datos del sensor de luz
        SharedPreferences sp = getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Luz", luz);
        editor.apply();
    }
}
