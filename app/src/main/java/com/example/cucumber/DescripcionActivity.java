package com.example.cucumber;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DescripcionActivity extends AppCompatActivity {

    private Planta planta;

    private TextView txtRiego;
    private TextView txtMaceta;
    private TextView txtDescripcion;
    private TextView txtExtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        configurarElementos();


        planta = (Planta)getIntent().getSerializableExtra("PlantaSeleccionada");

        Log.i("Cucumber", "onCreate");

        if(planta == null)
        {
            Toast.makeText(DescripcionActivity.this, "No se encontró la planta seleccionada", Toast.LENGTH_LONG);
        }
        else
            cargarDatosPlanta();
    }

    private void configurarElementos()
    {
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtExtras = findViewById(R.id.txtExtras);
        txtMaceta = findViewById(R.id.lblMaceta);
        txtRiego = findViewById(R.id.lblFrecuenciaRiego);
    }

    private void cargarDatosPlanta()
    {
        String extras = "\n\n";



        //getActionBar().setTitle(planta.getNombre());

        txtDescripcion.setText(planta.getDescripcion());
        txtMaceta.setText(planta.getMaceta());
        txtRiego.setText(planta.getRiego());

        for(String e : planta.getExtra())
        {
            extras.concat(e + "\n");
        }

        txtExtras.setText(extras);

        //Log.i("Cucumber", "Cargar datos de planta: " + planta.getNombre());

    }
}
