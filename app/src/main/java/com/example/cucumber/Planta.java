package com.example.cucumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Planta implements Serializable {

    private String idPlanta;
    private String nombre;
    private String descripcion;
    private String riego;
    private String maceta;
    private ArrayList<Integer> plantar;
    private  ArrayList<Integer> cosechar;
    private ArrayList<String> extra;
    private String imagen;

    public Planta()
    {}

    public Planta(String id, String nombre, String desc, String riego, String maceta, ArrayList<Integer> plantar, ArrayList<Integer> cosechar, String imagen)
    {
        idPlanta=id;
        this.nombre = nombre;
        this.descripcion = desc;
        this.riego = riego;
        this.maceta = maceta;
        this.plantar = plantar;
        this.cosechar = cosechar;
        this.extra = new ArrayList<String>();
        this.imagen = imagen;
    }

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

    public void setPlantar(ArrayList<Integer> plantar)
    {
        this.plantar = plantar;
    }

    public ArrayList<Integer> getPlantar()
    {
        return plantar;
    }

    public void setCosechar(ArrayList<Integer> cosechar)
    {
        this.cosechar = cosechar;
    }

    public ArrayList<Integer> getCosechar()
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
