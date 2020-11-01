package com.example.cucumber.Firebase;

import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static FirebaseDatabase database;


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
