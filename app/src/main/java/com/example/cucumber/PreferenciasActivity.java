package com.example.cucumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PreferenciasActivity extends AppCompatActivity {

    private TextView txtAcelerometro, txtProximidad, txtLuz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        configurarElementos();
        cargarPreferencias();
    }

    @Override
    protected void onPause() {

        Log.i("Cucumber | PrefAct", "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        Log.i("Cucumber | PrefAct", "onDestroy");
        super.onDestroy();
    }

    private void configurarElementos()
    {
        txtAcelerometro = findViewById(R.id.txtAcelerometro);
        txtProximidad= findViewById(R.id.txtProximidad);
        txtLuz = findViewById(R.id.txtLuz);
    }


    private void cargarPreferencias() {
        SharedPreferences preferences = this.getSharedPreferences("datos", Context.MODE_PRIVATE);
        float acelerometro = preferences.getFloat("Acelerometro", 0);
        float proximidad = preferences.getFloat("Proximidad", 0);
        float luz = preferences.getFloat("Luz", 0);

        txtAcelerometro.setText(Float.toString(acelerometro));
        txtProximidad.setText(Float.toString(proximidad));
        txtLuz.setText(Float.toString(luz));
    }
}
