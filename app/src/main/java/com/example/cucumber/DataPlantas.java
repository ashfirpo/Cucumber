package com.example.cucumber;

import com.example.cucumber.Firebase.Planta;

import java.util.List;

public class DataPlantas {

    private List<Planta> plantas;


    public void agregarPlanta(Planta p)
    {
        plantas.add(p);
    }

    public List<Planta> getPlantas()
    {
        return plantas;
    }

    public Planta getPlanta(String idPlanta)
    {
        for (Planta p : plantas)
        {
            if(p.getIdPlanta().compareTo(idPlanta)==0)
                return p;
        }
        return null;
    }

    public int getCantidad()
    {
        return plantas.size();
    }

}
