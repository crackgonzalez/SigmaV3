package com.crackgonzalez.sigmav3.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.crackgonzalez.sigmav3.actividades.PrincipalActivity;
import com.crackgonzalez.sigmav3.R;
import com.crackgonzalez.sigmav3.modelos.Servicio;
import com.crackgonzalez.sigmav3.viewholders.ServicioViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class ServicioFragment extends Fragment {

    private static final String SERVICIOS = "servicios" ;
    private static final String USUARIOSERVICIOS = "usuario-servicios";
    private DatabaseReference mDatabase;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Servicio, ServicioViewHolder> mAdaptador;

    private FloatingActionButton mFabAgregarServicio;

    public ServicioFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicio, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mRecycler = view.findViewById(R.id.rcl_servicios);
        mRecycler.setHasFixedSize(true);

        mFabAgregarServicio = ((PrincipalActivity) getActivity()).getFab();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdaptador != null) {
            mAdaptador.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdaptador != null) {
            mAdaptador.stopListening();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    mFabAgregarServicio.hide();
                }else if(dy<0){
                    mFabAgregarServicio.show();
                }
            }
        });

        Query serviciosQuery = obtenerConsulta(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Servicio>()
                .setQuery(serviciosQuery, Servicio.class)
                .build();

        mAdaptador = new FirebaseRecyclerAdapter<Servicio, ServicioViewHolder>(options) {

            @Override
            public ServicioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new ServicioViewHolder(inflater.inflate(R.layout.item_servicio, parent, false));
            }

            @Override
            protected void onBindViewHolder(final ServicioViewHolder holder, int position, Servicio model) {
                DatabaseReference servicioRef = getRef(position);
                final String servicioKey = servicioRef.getKey();
                final String uid = obtenerUid();

                holder.bindToServicio(model);

                holder.mImvMenuCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(getContext(), holder.mImvMenuCardView);
                        popupMenu.inflate(R.menu.menu_cardview);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.item_menu_eliminar:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setMessage("¿Desea eliminar el servicio?")
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        eliminarServicio(servicioKey,uid);
                                                    }
                                                })
                                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                        builder.create();
                                        builder.show();
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }
        };

        mRecycler.setAdapter(mAdaptador);
    }

    private void eliminarServicio(String key, String uid){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(SERVICIOS+ "/" + key, null);
        childUpdates.put(USUARIOSERVICIOS+ "/" + uid + "/" + key, null);
        mDatabase.updateChildren(childUpdates);
    }

    private Query obtenerConsulta(DatabaseReference bd) {
        return bd.child("usuario-servicios")
                .child(obtenerUid()).orderByChild("fecha").limitToLast(45);
    }

    @NonNull
    private String obtenerUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
