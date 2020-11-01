package com.example.cucumber;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.cucumber.Firebase.Planta;
import java.util.ArrayList;
import java.util.List;

public class ListadoPlantasActivity extends AppCompatActivity implements SensorEventListener {

    private GridView listado;
    private PlantasAdapter adapter;
    private List<Planta> plantas, plantasFav;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantas);

        plantas = new ArrayList<Planta>();

        plantas = (ArrayList<Planta>)getIntent().getSerializableExtra("listadoPlantas");
        plantasFav = (ArrayList<Planta>)getIntent().getSerializableExtra("plantasFav");

        /*database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        ref = database.getReference("/plantas");
        ref.keepSynced(true);*/

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        inicializarSensores();
        configurarElementos();
        cargarVista();

        Log.i("Cucumber", "onCreate");
    }

    @Override
    protected void onStart() {
        inicializarSensores();
        super.onStart();
        Log.i("Cucumber", "onStart");
    }

    @Override
    protected void onPause() {
        pararSensores();
        super.onPause();
        Log.i("Cucumber", "onPause");
    }

    @Override
    protected void onResume() {
        inicializarSensores();
        super.onResume();
        Log.i("Cucumber", "onResume");
    }

    @Override
    protected void onStop() {
        pararSensores();
        super.onStop();
        Log.i("Cucumber", "onStop");
    }

    @Override
    protected void onDestroy() {
        pararSensores();
        super.onDestroy();
        Log.i("Cucumber", "onDestroy");
    }


    private void inicializarSensores()
    {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void pararSensores()
    {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String valor;
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    valor = event.values[0] + " m/s^2\n";
                    Log.i("Cucumber | Acelerometro", valor);
                    guardarPreferenciasAcelerometro(event.values[0]);
                    break;

                case Sensor.TYPE_PROXIMITY:
                    valor = event.values[0] + " cm\n";
                    Log.i("Cucumber | Prox", valor);
                    guardarPreferenciasAcelerometro(event.values[0]);
                    break;

                case Sensor.TYPE_LIGHT:
                    valor = event.values[0] + " Lux\n";
                    Log.i("Cucumber | Luz", valor);
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


    /*private ValueEventListener oneTimeListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            for (DataSnapshot item : snapshot.getChildren()) {

                //planta = item.getValue(Planta.class);

                Planta p = new Planta();

                p.setIdPlanta(item.child("idPlanta").getValue().toString());
                p.setNombre(item.child("nombre").getValue().toString());
                p.setDescripcion(item.child("descripcion").getValue().toString());
                p.setRiego(item.child("riego").getValue().toString());
                p.setMaceta(item.child("maceta").getValue().toString());


                ArrayList<Integer> array_plantar = new ArrayList<>();
                for(DataSnapshot plantar : item.child("plantar").getChildren())
                {
                    array_plantar.add(plantar.getValue(Integer.class));
                }
                p.setPlantar(array_plantar);


                ArrayList<Integer> array_cosechar = new ArrayList<>();
                for(DataSnapshot cosechar : item.child("cosechar").getChildren())
                {
                    array_cosechar.add(cosechar.getValue(Integer.class));
                }
                p.setCosechar(array_cosechar);


                ArrayList<String> array_extras = new ArrayList<>();
                for(DataSnapshot ex: item.child("extra").getChildren())
                {
                    array_extras.add(ex.getValue(String.class));
                }
                p.setExtra(array_extras);


                p.setImagen(item.child("imagen").getValue().toString());


                plantas.add(p);
            }

            cargarVista();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }

    };*/

    private void cargarVista()
    {
        adapter = new PlantasAdapter(ListadoPlantasActivity.this, plantas);
        listado.setAdapter(adapter);

        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListadoPlantasActivity.this, DescripcionActivity.class);
                i.putExtra("PlantaSeleccionada",((Planta)listado.getItemAtPosition(position)));
                i.putExtra("plantasFav", (ArrayList<Planta>)plantasFav);

                startActivity(i);
            }
        });
    }



    private void guardarPreferenciasAcelerometro(float g)
    {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Acelerometro", g);
        editor.apply();
    }

    private void guardarPreferenciasLuz(float luz)
    {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("Luz", luz);
        editor.apply();
    }
}
