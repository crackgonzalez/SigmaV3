package com.crackgonzalez.sigmav3.actividades;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crackgonzalez.sigmav3.R;
import com.crackgonzalez.sigmav3.clases.BaseActivity;
import com.crackgonzalez.sigmav3.clases.Constante;
import com.crackgonzalez.sigmav3.modelos.Servicio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.crackgonzalez.sigmav3.clases.Constante.SERVICIOS;
import static com.crackgonzalez.sigmav3.clases.Constante.USUARIOSERVICIOS;

public class ActualizarServicioActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEdtSiglaUdp;
    private EditText mEdtHisUdp;
    private EditText mEdtHtsUdp;
    private EditText mEdtCargaUdp;
    private EditText mEdtFechaUdp;
    private Button mBtnModificarServicio;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_servicio);

        mEdtSiglaUdp = findViewById(R.id.edt_sigla_udp);
        mEdtHisUdp = findViewById(R.id.edt_his_udp);
        mEdtHtsUdp = findViewById(R.id.edt_hts_udp);
        mEdtCargaUdp = findViewById(R.id.edt_carga_udp);
        mEdtFechaUdp = findViewById(R.id.edt_fecha_udp);

        mEdtHisUdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarTimePickerDialog(hasFocus, mEdtHisUdp);
            }
        });
        mEdtHtsUdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarTimePickerDialog(hasFocus, mEdtHtsUdp);
            }
        });
        mEdtCargaUdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarTimePickerDialog(hasFocus, mEdtCargaUdp);
            }
        });
        mEdtFechaUdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mostrarDatePickerDialog(hasFocus, mEdtFechaUdp);
            }
        });

        mBtnModificarServicio = findViewById(R.id.btn_modificar_servicio);
        mBtnModificarServicio.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        cargarDatos();
    }

    private void cargarDatos(){
        String uid = obtenerUid();
        String servicio = getIntent().getExtras().getString("servicioKey");
        DatabaseReference ServicioRef = mDatabase.child(USUARIOSERVICIOS).child(uid).child(servicio);
        ServicioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Servicio servicio = dataSnapshot.getValue(Servicio.class);
                mEdtSiglaUdp.setText(servicio.sigla);
                mEdtHisUdp.setText(decimalHora(servicio.his));
                mEdtHtsUdp.setText(decimalHora(servicio.hts));
                mEdtCargaUdp.setText(decimalHora(servicio.carga));
                mEdtFechaUdp.setText(longFecha(servicio.fecha));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void modificarServicio(){

        final String sigla = mEdtSiglaUdp.getText().toString();
        double his = horaDecimal(mEdtHisUdp.getText().toString());
        double hts = horaDecimal(mEdtHtsUdp.getText().toString());
        double carga = horaDecimal(mEdtCargaUdp.getText().toString());
        long fecha = fechaLong(mEdtFechaUdp.getText().toString());
        String uid = obtenerUid();
        String key = getIntent().getExtras().getString("servicioKey");

        Servicio servicio = new Servicio(sigla,his,hts,carga,fecha,uid);

        Map<String, Object> servicioValues = servicio.servicioMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(SERVICIOS+ "/" + key, servicioValues);
        childUpdates.put(USUARIOSERVICIOS+ "/" + uid + "/" + key, servicioValues);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mostrarMensajeToastLargo(getBaseContext(),"El servicio "+ sigla + " fue modificado correctamente");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMensajeToastLargo(getBaseContext(),"Ocurrio un error, verifique los datos ingresados");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_modificar_servicio) {
            modificarServicio();
            finish();
        }
    }
}
