package com.crackgonzalez.sigmav3;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public long fechaLong(String fechaString){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        long fechaLong = 0;
        try {
            Date fecha  = sdf.parse(fechaString+" 03:00");
            fechaLong = fecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaLong;
    }

    public double horaDecimal(String horaString){
        String[] partes = horaString.split(":");
        double hora = Double.parseDouble(partes[0]);
        double minutos = Double.parseDouble(partes[1]);
        double resultado = Math.round((hora+(minutos/60))*100.0)/100.0;
        return resultado;
    }

}
