package com.crackgonzalez.sigmav3.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.crackgonzalez.sigmav3.R;
import com.crackgonzalez.sigmav3.clases.BaseActivity;

public class DetalleServicioActivity extends BaseActivity implements View.OnClickListener{

    private FloatingActionButton mFabAddVuelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);

        mFabAddVuelta = findViewById(R.id.fab_agregar_vuelta);
        mFabAddVuelta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fab_agregar_vuelta) {
            iniciarActividadVuelta();
        }
    }

    private void iniciarActividadVuelta() {
        Intent intent = new Intent(this,CrearVueltaActivity.class);
        startActivity(intent);
    }
}
