package com.example.marplex.schoolbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marplex.schoolbook.R;
import com.example.marplex.schoolbook.adapters.SectionPagerAdapter;
import com.example.marplex.schoolbook.fragments.custom.DrawerFragment;
import com.example.marplex.schoolbook.fragments.tabs.Home;
import com.example.marplex.schoolbook.fragments.tabs.Reminds;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends DrawerFragment {

    @Bind(R.id.pager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);

        /**
         * @see SectionPagerAdapter
         */
        pager.setAdapter(new SectionPagerAdapter(this.getChildFragmentManager(), new Home(), new Reminds()));

        /**
         * @see DrawerFragment
         */
        setToolbarLayout(pager);

        return rootView;
    }

    @Override
    public String getTitle() {
        return "Dashboard";
    }

}
