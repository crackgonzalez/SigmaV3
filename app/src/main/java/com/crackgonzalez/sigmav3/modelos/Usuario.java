package com.crackgonzalez.sigmav3.modelos;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {

    public String nombre;
    public String email;

    public Usuario() {
    }

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }
}