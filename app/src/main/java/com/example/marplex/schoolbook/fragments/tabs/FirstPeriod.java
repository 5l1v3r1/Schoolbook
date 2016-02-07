package com.example.marplex.schoolbook.fragments.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPeriod extends Fragment {


    public FirstPeriod() { }

    public static FirstPeriod newInstance() {
        return new FirstPeriod();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_period, container, false);
    }


}
