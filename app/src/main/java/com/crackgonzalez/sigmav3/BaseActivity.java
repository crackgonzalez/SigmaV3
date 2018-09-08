package com.crackgonzalez.sigmav3;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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

    public String obtenerUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void mostrarMensajeToastLargo(Context contexto , String mensaje){
        Toast.makeText(contexto,mensaje,Toast.LENGTH_LONG).show();
    }

    public void mostrarMensajeToastCorto(Context contexto , String mensaje){
        Toast.makeText(contexto,mensaje,Toast.LENGTH_SHORT).show();
    }

}
