package com.example.cucumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListadoPlantasActivity extends AppCompatActivity {

    private GridView listado;
    private PlantasAdapter adapter;

    private List<Planta> plantas;

    public FirebaseDatabase database;
    public DatabaseReference ref, refPlantar, refCosechar, refExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantas);


        plantas = new ArrayList<Planta>();


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("/plantas");


        configurarElementos();
        //cargarVista();
    }

    private void configurarElementos()
    {
        listado = findViewById(R.id.gridPlantas);

        setearListeners();
    }

    private void setearListeners()
    {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              // try {

                    for (DataSnapshot item : snapshot.getChildren()) {

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
                    /*


                }
                catch (Exception e)
                {
                    Toast.makeText(ListadoPlantasActivity.this, "CATCH: "+ e.getMessage(), Toast.LENGTH_LONG);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void cargarVista()
    {
        adapter = new PlantasAdapter(ListadoPlantasActivity.this, plantas);
        listado.setAdapter(adapter);

        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListadoPlantasActivity.this, DescripcionActivity.class);
                i.putExtra("PlantaSeleccionada",((Planta)listado.getItemAtPosition(position)));

                startActivity(i);

                /*if (listado.getItemAtPosition(position) != null) {
                    String texto = ((Planta)listado.getItemAtPosition(position)).getNombre();

                    Toast.makeText(ListadoPlantasActivity.this, "Seleccionado: " + texto, Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(ListadoPlantasActivity.this, "NULO | POS: " + position, Toast.LENGTH_SHORT).show();*/
            }
        });
    }
}
