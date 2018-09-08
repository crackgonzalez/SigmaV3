package com.crackgonzalez.sigmav3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crackgonzalez.sigmav3.modelos.Servicio;
import com.crackgonzalez.sigmav3.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CrearServicioActivity extends BaseActivity implements View.OnClickListener {

    private static final String USUARIOS = "usuarios";
    private static final String USUARIO = "usuario";
    private static final String SERVICIOS = "servicios";

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

        mBtnAgregarServicio = findViewById(R.id.btn_agregar_servicio);
        mBtnAgregarServicio.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_agregar_servicio) {
            enviarServicio();
        }
    }

    private void crearServicio(String sigla, double his, double hts, double carga, long fecha){
        String servicioKey = mDatabase.child(SERVICIOS).push().getKey();
        String usuarioKey = obtenerUid();

        Servicio servicio = new Servicio(sigla,his,hts,carga,fecha);

        mDatabase.child(SERVICIOS).child(servicioKey).setValue(servicio);
        mDatabase.child(SERVICIOS).child(servicioKey).child(USUARIO).child(usuarioKey).setValue(true);
        mDatabase.child(USUARIOS).child(usuarioKey).child(SERVICIOS).child(servicioKey).setValue(true);

    }

    private void enviarServicio(){
        String usuarioKey = obtenerUid();
        final String sigla = mEdtSigla.getText().toString();
        final double his = horaDecimal(mEdtHis.getText().toString());
        final double hts = horaDecimal(mEdtHts.getText().toString());
        final double carga = horaDecimal(mEdtCarga.getText().toString());
        final long fecha = fechaLong(mEdtFecha.getText().toString());

        mDatabase.child(USUARIOS).child(usuarioKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario != null) {
                    crearServicio(sigla,his,hts,carga,fecha);
                    mostrarMensajeToastLargo(CrearServicioActivity.this,"Servicio "+ sigla + " Agregado");
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mostrarMensajeToastLargo(CrearServicioActivity.this, "El servicio no pudo ser agregado");
            }
        });
    }
}
