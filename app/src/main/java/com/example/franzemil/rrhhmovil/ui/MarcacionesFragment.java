package com.example.franzemil.rrhhmovil.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.franzemil.rrhhmovil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarcacionesFragment extends Fragment {


    public MarcacionesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marcaciones, container, false);
    }

}
