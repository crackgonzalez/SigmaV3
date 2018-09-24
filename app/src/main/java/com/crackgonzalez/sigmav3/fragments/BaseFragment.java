package com.crackgonzalez.sigmav3.fragments;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class BaseFragment extends Fragment {

    public String obtenerUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
