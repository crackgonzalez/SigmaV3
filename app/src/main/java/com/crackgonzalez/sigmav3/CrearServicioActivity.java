package com.crackgonzalez.sigmav3;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crackgonzalez.sigmav3.modelos.Servicio;
import com.crackgonzalez.sigmav3.modelos.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CrearServicioActivity extends BaseActivity implements View.OnClickListener {

    private static final String USUARIOS = "usuarios";
    private static final String USUARIOSERVICIOS = "usuario-servicios";
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
            crearServicio();
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
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMensajeToastLargo(getBaseContext(),"Ocurrio un error, verifique los datos ingresados");
            }
        });
    }
}
