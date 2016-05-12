package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.fragments.custom.DrawerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends DrawerFragment {

    private View mView;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_about, container, false);

        setTabGone();
        removeMenuItems();

        return mView;
    }

    @Override
    public String getTitle() {
        return "About";
    }
}
