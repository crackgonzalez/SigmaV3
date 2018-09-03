package com.crackgonzalez.sigmav3;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mBarraDeProgreso;

    public void mostrarProgressDialog() {
        if (mBarraDeProgreso == null) {
            mBarraDeProgreso = new ProgressDialog(this);
            mBarraDeProgreso.setCancelable(false);
            mBarraDeProgreso.setMessage("Cargando...");
        }

        mBarraDeProgreso.show();
    }

    public void ocultarProgressDialog() {
        if (mBarraDeProgreso != null && mBarraDeProgreso.isShowing()) {
            mBarraDeProgreso.dismiss();
        }
    }

}
