package com.crackgonzalez.sigmav3.actividades;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SigmaV3 extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
