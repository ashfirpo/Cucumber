package com.example.cucumber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    private Button btnListadoPlantas;
    private Button btnJardin;
    private Button btnPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        configurarElementos();
    }

    private void configurarElementos()
    {
        btnListadoPlantas = findViewById(R.id.btnListadoPlantas);
        btnJardin = findViewById(R.id.btnJardin);
        btnPreferencias = findViewById(R.id.btnPreferencias);

        setearListeners();
    }

    private void setearListeners()
    {
        btnListadoPlantas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ListadoPlantasActivity.class);
                startActivity(i);
            }
        });


        btnJardin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, MiJardinActivity.class);
                startActivity(i);
            }
        });


        btnPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, PreferenciasActivity.class);
                startActivity(i);
            }
        });
    }

}