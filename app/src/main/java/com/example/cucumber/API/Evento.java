package com.example.cucumber.API;

public class Evento {

    private String type_event;
    private long dni;
    private String description;
    private long id;


    public String getType_event()
    {
        return type_event;
    }

    public void setType_event(String type_event)
    {
        this.type_event = type_event;
    }

    public long getDni()
    {
        return dni;
    }

    public void setDni(long dni)
    {
        this.dni = dni;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
