package com.example.cucumber.Firebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Planta implements Serializable {

    private String idPlanta;
    private String nombre;
    private String descripcion;
    private String riego;
    private String maceta;
    private ArrayList<String> plantar = new ArrayList<>();
    private ArrayList<String> cosechar = new ArrayList<>();
    private ArrayList<String> extra = new ArrayList<>();
    private String imagen;

    public Planta()
    {}


    public void setIdPlanta(String idPlanta)
    {
        this.idPlanta = idPlanta;
    }

    public String getIdPlanta()
    {
        return idPlanta;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion=descripcion;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setRiego(String riego)
    {
        this.riego=riego;
    }

    public String getRiego()
    {
        return riego;
    }

    public void setMaceta(String maceta)
    {
        this.maceta = maceta;
    }

    public String getMaceta()
    {
        return maceta;
    }

    public void setPlantar(ArrayList<String> plantar)
    {
        this.plantar = plantar;
    }

    public ArrayList<String> getPlantar()
    {
        return plantar;
    }

    public void setCosechar(ArrayList<String> cosechar)
    {
        this.cosechar = cosechar;
    }

    public ArrayList<String> getCosechar()
    {
        return cosechar;
    }

    public void setExtra(ArrayList<String> extras)
    {
        this.extra = extras;
    }

    public List<String> getExtra()
    {
        return this.extra;
    }

    public void setImagen(String imagen)
    {
        this.imagen = imagen;
    }

    public String getImagen()
    {
        return this.imagen;
    }
}
