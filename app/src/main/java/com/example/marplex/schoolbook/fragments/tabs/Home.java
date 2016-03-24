package com.example.marplex.schoolbook.fragments.tabs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.fragments.custom.PagerFragment;

public class Home extends PagerFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);
        return rootView;
    }

    @Override
    public String getPageTitle() {
        return "Home";
    }
}
