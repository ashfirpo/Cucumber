package com.example.cucumber.API;

public class SOAEventResponse {

    private boolean success;
    private String env;
    private Evento event;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getEnv()
    {
        return env;
    }

    public void setEnv(String env)
    {
        this.env = env;
    }

    public Evento getEvent()
    {
        return event;
    }

    public void setEvent(Evento event)
    {
        this.event = event;
    }


}
