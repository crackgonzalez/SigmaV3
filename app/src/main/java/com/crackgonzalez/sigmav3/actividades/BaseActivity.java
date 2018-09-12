package com.crackgonzalez.sigmav3.actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        long fechaLong = -1;
        try {
            Date fecha  = sdf.parse(fechaString+" 03:00");
            fechaLong = fecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaLong;
    }

    public double horaDecimal(String horaString){
        double resultado =-1;
        try {
            String[] partes = horaString.split(":");
            double hora = Double.parseDouble(partes[0]);
            double minutos = Double.parseDouble(partes[1]);
            resultado = Math.round((hora + (minutos / 60)) * 100.0) / 100.0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public String decimalHora(double horaDecimal){
        String horaString = String.valueOf(horaDecimal);
        String[] partes = horaString.split("\\.");
        int hora = Integer.parseInt(partes[0]);
        int minutos = (int)Math.round((horaDecimal - hora)*60);
        String resultado = "";
        if(minutos>=0 &&minutos<=9){
            resultado = hora +":0"+ minutos;
        }else{
            resultado = hora+":"+minutos;
        }
        return resultado;
    }

    public String longFecha(long fechaLong){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date  = new Date(fechaLong);
        return sdf.format(date);
    }

    public void mostrarTimePickerDialog(boolean focus, final TextView textView){
        Calendar calendar = GregorianCalendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(minute>=0 && minute<=9){
                    textView.setText(hourOfDay+":0"+minute);
                }else{
                    textView.setText(hourOfDay+":"+minute);
                }
            }
        },hora,minutos,true);
        if(focus){
            tpd.setTitle(textView.getHint());
            tpd.show();
            textView.requestFocus(View.FOCUS_FORWARD);
        }else{
            tpd.hide();
        }
    }

    public void mostrarDatePickerDialog(boolean focus, final TextView textView){
        Calendar calendar = Calendar.getInstance();
        int anno = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },anno,mes,dia);
        if(focus){
            dpd.setTitle(textView.getHint());
            dpd.show();
            textView.requestFocus(View.FOCUS_FORWARD);
        }else{
            dpd.hide();
        }
    }

}
