package com.example.cucumber.Firebase;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String id;
    private String email;
    private ArrayList<String> plantasFav = new ArrayList<>();


    public Usuario()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public ArrayList<String> getPlantasFav()
    {
        return plantasFav;
    }

    public void setPlantasFav(ArrayList<String> plantasFav)
    {
        this.plantasFav = plantasFav;
    }

    public boolean setPlantaFav(String idPlanta)
    {
        for(String planta : plantasFav)
        {
            if(planta.equals(idPlanta))
                return false;
        }
        plantasFav.add(idPlanta);
        return true;
    }

}
