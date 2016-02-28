package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.marplex.schoolbook.DashboardActivity;

public class DashboardFragment extends Fragment {

    //DashboardActivity activity
    protected DashboardActivity mainActivity;

    public DashboardFragment() {}

    @Override
    public void onCreate (Bundle savedInstanceState){
        mainActivity = ((DashboardActivity) getActivity());
        onCreate(savedInstanceState);
    }

}
