package com.example.cucumber;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cucumber.API.SessionManager;
import com.example.cucumber.Firebase.Firebase;
import com.example.cucumber.Firebase.Planta;
import com.example.cucumber.Firebase.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    private Button btnListadoPlantas, btnJardin, btnPreferencias;
    private SessionManager sessionManager;
    private DatabaseReference refUser, refPlantas;
    private Usuario user;
    private ArrayList<Planta> plantas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(getApplicationContext());
        verificarBateria();

        refPlantas = Firebase.getFirebaseDatabase().getReference("/plantas");
        refPlantas.keepSynced(true);
        refUser = Firebase.getFirebaseDatabase().getReference("/users");

        refPlantas.addListenerForSingleValueEvent(plantasListener);
        refUser.addValueEventListener(usersListener);

        configurarElementos();
    }

    private void verificarBateria()
    {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent estadoBateria = getApplicationContext().registerReceiver(null, intentFilter);
        int nivel = estadoBateria.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int escala = estadoBateria.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int porcentaje = nivel * 100 / escala;
        String bateria;

        if(porcentaje >= 80)
            bateria = "%: Batería alta";
        else if(porcentaje <= 20)
            bateria = "%: Batería baja";
        else
            bateria = "%: Batería normal";

        Toast.makeText(this, porcentaje + bateria, Toast.LENGTH_SHORT).show();
    }

    private void configurarElementos()
    {
        btnListadoPlantas = findViewById(R.id.btnListadoPlantas);
        btnJardin = findViewById(R.id.btnJardin);
        btnPreferencias = findViewById(R.id.btnPreferencias);

        btnJardin.setOnClickListener(botonListener);
        btnListadoPlantas.setOnClickListener(botonListener);
        btnPreferencias.setOnClickListener(botonListener);
    }


    //Listeners

    private View.OnClickListener botonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i;

            switch (v.getId())
            {
                case R.id.btnListadoPlantas:
                    i = new Intent(HomeActivity.this, ListadoPlantasActivity.class);
                    i.putExtra("listadoPlantas", plantas); //--> ver de sacar este
                    i.putExtra("plantasFav", user.getPlantasFav());
                    startActivity(i);
                    break;

                case R.id.btnJardin:
                    i = new Intent(HomeActivity.this, MiJardinActivity.class);
                    i.putExtra("plantasFav", user.getPlantasFav());
                    startActivity(i);
                    break;

                case R.id.btnPreferencias:
                    i = new Intent(HomeActivity.this, PreferenciasActivity.class);
                    startActivity(i);
                    break;

                default:
                    Toast.makeText(HomeActivity.this, "Error en los botones", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    private ValueEventListener usersListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            //Obtenemos el mail con el que se inició sesión
            //String emailLogin = sessionManager.getEmail();
            String emailLogin = "ayelenfirpo@gmail.com";
            String emailDB;

            //Se busca al usuario en la base de datos de Firebase
            for(DataSnapshot item : snapshot.getChildren())
            {
                emailDB = item.child("email").getValue().toString();
                if(emailDB.equals(emailLogin))
                {
                    user = item.getValue(Usuario.class);
                    user.setId(item.getKey());
                    return;
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private ValueEventListener plantasListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            //Cargamos todas las plantas disponibles
            for (DataSnapshot item : snapshot.getChildren()) {
                Planta p = item.getValue(Planta.class);

                /*Planta p = new Planta();

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
*/

                plantas.add(p);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}