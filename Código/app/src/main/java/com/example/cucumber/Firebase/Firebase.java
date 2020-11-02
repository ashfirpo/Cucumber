package com.example.cucumber.Firebase;

import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static FirebaseDatabase database;

    //Si bien de por sí Firebase se instancia como Singleton, hacemos esto para que la persistencia sea
    //habilitada una única vez
    private Firebase()
    {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    public static FirebaseDatabase getFirebaseDatabase()
    {
        if(database == null)
            new Firebase();
        return database;
    }
}
