package com.example.marplex.schoolbook.fragments.tabs;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPeriod extends Fragment {


    public FirstPeriod() {
        // Required empty public constructor
    }

    public static FirstPeriod newInstance() {
        return new FirstPeriod();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_period, container, false);
        return rootView;
    }

}
