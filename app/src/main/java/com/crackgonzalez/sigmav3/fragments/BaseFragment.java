package com.crackgonzalez.sigmav3.fragments;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseFragment extends Fragment {

    public String obtenerUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
