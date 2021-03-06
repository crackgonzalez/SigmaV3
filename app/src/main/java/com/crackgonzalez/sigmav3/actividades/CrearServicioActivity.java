package com.crackgonzalez.sigmav3.actividades;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crackgonzalez.sigmav3.R;
import com.crackgonzalez.sigmav3.clases.BaseActivity;
import com.crackgonzalez.sigmav3.modelos.Servicio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.crackgonzalez.sigmav3.clases.Constante.SERVICIOS;
import static com.crackgonzalez.sigmav3.clases.Constante.USUARIOSERVICIOS;

public class CrearServicioActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEdtSigla;
    private EditText mEdtHis;
    private EditText mEdtHts;
    private EditText mEdtCarga;
    private EditText mEdtFecha;
    private Button mBtnAgregarServicio;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_servicio);

        mEdtSigla = findViewById(R.id.edt_sigla);
        mEdtHis = findViewById(R.id.edt_his);
        mEdtHts = findViewById(R.id.edt_hts);
        mEdtCarga = findViewById(R.id.edt_carga);
        mEdtFecha = findViewById(R.id.edt_fecha);

        mEdtHis.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarTimePickerDialog(hasFocus, mEdtHis);
            }
        });
        mEdtHts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarTimePickerDialog(hasFocus, mEdtHts);
            }
        });
        mEdtCarga.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarTimePickerDialog(hasFocus, mEdtCarga);
            }
        });
        mEdtFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarDatePickerDialog(hasFocus, mEdtFecha);
            }
        });

        mBtnAgregarServicio = findViewById(R.id.btn_agregar_servicio);
        mBtnAgregarServicio.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_agregar_servicio) {
            crearServicio();
            finish();
        }
    }

    private void crearServicio(){

        final String sigla = mEdtSigla.getText().toString();
        double his = horaDecimal(mEdtHis.getText().toString());
        double hts = horaDecimal(mEdtHts.getText().toString());
        double carga = horaDecimal(mEdtCarga.getText().toString());
        long fecha = fechaLong(mEdtFecha.getText().toString());
        String uid = obtenerUid();

        Servicio servicio = new Servicio(sigla,his,hts,carga,fecha,uid);

        Map<String, Object> servicioValues = servicio.servicioMap();
        Map<String, Object> childUpdates = new HashMap<>();

        String key = mDatabase.child(SERVICIOS).push().getKey();

        childUpdates.put(SERVICIOS+ "/" + key, servicioValues);
        childUpdates.put(USUARIOSERVICIOS+ "/" + uid + "/" + key, servicioValues);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mostrarMensajeToastLargo(getBaseContext(),"El servicio "+ sigla + " fue agregado correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMensajeToastLargo(getBaseContext(),"Ocurrio un error, verifique los datos ingresados");
            }
        });
    }
}
