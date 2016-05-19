package com.marco.marplex.schoolbook.fragments.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marco.marplex.schoolbook.R;
import com.marco.marplex.schoolbook.fragments.custom.PagerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reminds extends PagerFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        return rootView;
    }

    @Override
    public String getPageTitle() {
        return "Reminds";
    }
}
