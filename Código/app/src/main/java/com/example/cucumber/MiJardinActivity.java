package com.example.cucumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class MiJardinActivity extends AppCompatActivity {

    private GridView miJardin;
    private PlantasAdapter adapter;
    private ArrayList<Planta> plantasFav;
    private Usuario user;
    private DatabaseReference refUserPlantas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_jardin);

        //Iniciallizamos las plantas recibidas
        plantasFav = new ArrayList<>();
        plantasFav = (ArrayList<Planta>)getIntent().getSerializableExtra("plantasFav");
        user = (Usuario)getIntent().getSerializableExtra("user");

        //Instanciamos las referencias a la base de datos que vamos a usar
        refUserPlantas = Firebase.getFirebaseDatabase().getReference("/users");
        refUserPlantas.keepSynced(true);
        refUserPlantas.addValueEventListener(userListener);

        configurarElementos();
        cargarVista();
    }

    private void configurarElementos()
    {
        miJardin = findViewById(R.id.gridMiJardin);
    }

    private void cargarVista()
    {
        //Setemos el adapter para las plantas a la grilla
        adapter = new PlantasAdapter(getApplicationContext(), plantasFav);
        miJardin.setAdapter(adapter);
        miJardin.setOnItemClickListener(grillaListener);
    }

    private AdapterView.OnItemClickListener grillaListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Abrimos el detalle de la planta favorita que seleccionamos
            Intent i = new Intent(getApplicationContext(), DescripcionActivity.class);
            i.putExtra("PlantaSeleccionada",((Planta)miJardin.getItemAtPosition(position)));
            i.putExtra("plantasFav", Planta.obtenerObjetosPlanta(plantasFav, user.getPlantasFav()));
            i.putExtra("user", user);

            startActivity(i);
        }
    };

    private ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            //Si hubo cambios en las plantas favoritas del usuario, actualizamos esos datos
            Usuario u = snapshot.child(user.getId()).getValue(Usuario.class);
            if(u.getPlantasFav() != null)
            {
                user.setPlantasFav(u.getPlantasFav());
                plantasFav = Planta.obtenerObjetosPlanta(plantasFav, user.getPlantasFav());
                cargarVista();
                return;
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), "Error en la actualización de datos", Toast.LENGTH_SHORT).show();
        }
    };
}
