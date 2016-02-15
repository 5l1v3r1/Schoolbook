package com.example.marplex.schoolbook.fragments.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;

public class Home extends Fragment{

    View rootView;
    RecyclerView list;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home, container, false);
        return rootView;
    }

    public static Home newInstance() {
        return new Home();
    }

}
